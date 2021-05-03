package com.example.orangemeet

import BackendCommunication
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.Response


class ContactsFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView


    lateinit var contactsListView : LinearLayout
    var contactsList = MutableLiveData<MutableList<Contact>>()
    lateinit var addContactButton : MenuItem

    var container : ViewGroup? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts, menu)

        addContactButton = menu.findItem(R.id.addContact)

        addContactButton.setOnMenuItemClickListener {
            /*parentFragmentManager.commit {
                replace<FindContactFragment>(container!!.id)
                setReorderingAllowed(true)
                addToBackStack("name") // name can be null
            }*/
            findNavController().navigate(R.id.nav_find_contact)
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container
        setHasOptionsMenu(true)

        val contactsFragment = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsListView = contactsFragment.findViewById<LinearLayout>(R.id.contactsList)
        progressBar = contactsFragment.findViewById(R.id.progressBar)
        searchBar = contactsFragment.findViewById(R.id.searchView)
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CreateContactViews(inflater)
                return true
            }
        })

        val deleteButton = contactsFragment.findViewById<ImageButton>(R.id.deleteButton)
        val addButton = contactsFragment.findViewById<ImageButton>(R.id.addButton)

        /*deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Usuwanie kontaktów")
                .setMessage(R.string.delete_contacts_dialog)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        contactsList.value!!.forEach{contact ->
                            if(contact.selected){
                                BackendCommunication.DeleteContact(requireContext(), contact.username,
                                        Response.Listener {
                                            Log.i("ContactsFragment", "DeletedContact successful")
                                            contactsList.value = contactsList.value!!.minus(contact).toMutableList()

                                            Toast.makeText(
                                                    context,
                                                    R.string.contacts_deleted_success,
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        Response.ErrorListener {
                                            Toast.makeText(
                                                    context,
                                                    R.string.contacts_deleted_fail,
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                        })
                            }
                        }

                    })
                .setNegativeButton(R.string.no, null).show()
        }*/

       /* addButton.setOnClickListener {
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(context)
                    .setTitle("Dodaj kontakt")
                    .setMessage(R.string.add_contact_dialog)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setView(input)
                    .setPositiveButton(R.string.yes,
                            DialogInterface.OnClickListener { dialog, whichButton ->
                                BackendCommunication.AddContact(requireContext(), input.text.toString(),
                                Response.Listener {
                                    Toast.makeText(
                                            context,
                                            R.string.add_contact_success,
                                            Toast.LENGTH_SHORT
                                    ).show()
                                    BackendCommunication.GetContactsList(
                                            requireContext(),
                                            Response.Listener { contactsList -> this.contactsList.value = contactsList.toMutableList() },
                                            null)
                                },
                                Response.ErrorListener {
                                    Toast.makeText(
                                            context,
                                            R.string.add_contact_fail,
                                            Toast.LENGTH_SHORT
                                    ).show()
                                })
                            })
                    .setNegativeButton(R.string.no, null).show()
        }
*/
        contactsList.observe(viewLifecycleOwner, Observer { CreateContactViews(inflater) })

        progressBar.visibility = View.VISIBLE
        BackendCommunication.GetContactsList(requireContext(),
                Response.Listener {contactsList -> this.contactsList.value = contactsList.toMutableList() },
                Response.ErrorListener { Log.e("ContactsFragment", "GetContactsList failed") })

        return contactsFragment
    }

    private fun CreateContactViews(inflater: LayoutInflater){
        //val filteredContacts = contactsList.value!!.filter { contact ->
        //TODO: Remove test contacts
        val filteredContacts = List<Contact>(13){i -> Contact()}.filter { contact ->
            if(searchBar.query.isEmpty())
                true
            else
                contact.username.toLowerCase().contains(searchBar.query.toString().toLowerCase())
        }

        contactsListView.removeAllViews()
        var primaryColor = TypedValue()
        var secondaryColor = TypedValue()
        context?.theme?.resolveAttribute(R.attr.background, primaryColor, true)
        //context?.theme?.resolveAttribute(R.attr.secondaryBackground, secondaryColor, true)
        context?.theme?.resolveAttribute(R.attr.background, secondaryColor, true)

        var useSecondaryColor : Boolean = false

        filteredContacts.forEach {
            val contact = it
            val color = ColorDrawable(resources.getColor(
                    if(useSecondaryColor == true)
                        primaryColor.resourceId
                    else
                        secondaryColor.resourceId))

            val view = Contact.createView(inflater, contactsListView, it, color)
            contactsListView.addView(view)
            useSecondaryColor = !useSecondaryColor
        }

        progressBar.visibility = View.GONE
    }
}