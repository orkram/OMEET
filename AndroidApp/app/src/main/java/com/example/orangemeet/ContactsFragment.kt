package com.example.orangemeet

import BackendCommunication
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
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
        contactsListView = contactsFragment.findViewById<LinearLayout>(R.id.contactsLayout)
        progressBar = contactsFragment.findViewById(R.id.progressBar)
        searchBar = contactsFragment.findViewById(R.id.searchView)
        searchBar.setOnClickListener { v -> searchBar.isIconified = false }
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CreateContactViews(inflater)
                return true
            }
        })

        contactsList.observe(viewLifecycleOwner, Observer { CreateContactViews(inflater) })

        progressBar.visibility = View.VISIBLE
        BackendCommunication.GetContactsList(requireContext(),
                Response.Listener {
                    contactsList -> this.contactsList.value = contactsList.toMutableList()
                    progressBar.visibility = View.GONE
                },
                Response.ErrorListener {error ->
                    Toast.makeText(context, "Nie udało się pobrać listy kontaktów", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                })

        return contactsFragment
    }

    private fun CreateContactViews(inflater: LayoutInflater){
        if(contactsList.value == null)
            return

        val filteredContacts = contactsList.value!!.filter { contact ->
            if(searchBar.query.isEmpty())
                true
            else
                contact.username.toLowerCase().contains(searchBar.query.toString().toLowerCase())
        }

        contactsListView.removeAllViews()

        filteredContacts.forEach {contact ->
            val view = Contact.createView(inflater, contactsListView, contact, null)
            view.setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(resources.getString(R.string.delete_from_contacts)).setOnMenuItemClickListener {

                    AlertDialog.Builder(requireContext())
                            .setTitle(R.string.delete_from_contacts)
                            .setMessage(R.string.delete_contact_dialog_message)
                            .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                                BackendCommunication.DeleteContact(requireContext(), contact.username,
                                Response.Listener {
                                    BackendCommunication.GetContactsList(requireContext(),
                                    Response.Listener {
                                        contactsList.value = it.toMutableList() },
                                    Response.ErrorListener {
                                        Toast.makeText(requireContext(), R.string.update_contacts_fail, Toast.LENGTH_LONG).show()
                                    })
                                },
                                Response.ErrorListener {
                                    Toast.makeText(requireContext(), R.string.contact_delete_fail, Toast.LENGTH_LONG).show()
                                })
                            })
                            .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, which -> })
                            .show()

                    true
                }
            }
            contactsListView.addView(view)
        }
    }
}