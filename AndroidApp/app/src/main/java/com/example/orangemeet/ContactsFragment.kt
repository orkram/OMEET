package com.example.orangemeet

import BackendCommunication
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.volley.Response


class ContactsFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : TextView


    lateinit var contactsListView : LinearLayout
    var contactsList = MutableLiveData<MutableList<Contact>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val contactsFragment = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsListView = contactsFragment.findViewById<LinearLayout>(R.id.contactsList)
        progressBar = contactsFragment.findViewById(R.id.progressBar)
        searchBar = contactsFragment.findViewById(R.id.searchBar)
        searchBar.doOnTextChanged { text, start, before, count ->
            CreateContactViews(inflater)
        }

        val deleteButton = contactsFragment.findViewById<ImageButton>(R.id.deleteButton)
        val addButton = contactsFragment.findViewById<ImageButton>(R.id.addButton)

        deleteButton.setOnClickListener {
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
        }

        addButton.setOnClickListener {
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

        contactsList.observe(viewLifecycleOwner, Observer { CreateContactViews(inflater) })

        progressBar.visibility = View.VISIBLE
        BackendCommunication.GetContactsList(requireContext(),
                Response.Listener {contactsList -> this.contactsList.value = contactsList.toMutableList() },
                Response.ErrorListener { Log.e("ContactsFragment", "GetContactsList failed") })

        return contactsFragment
    }

    private fun CreateContactViews(inflater: LayoutInflater){
        val filteredContacts = contactsList.value!!.filter { contact ->
            if(searchBar.text.isEmpty())
                true
            else
                contact.username.toLowerCase().contains(searchBar.text.toString().toLowerCase())
        }

        contactsListView.removeAllViews()
        var primaryColor = TypedValue()
        var secondaryColor = TypedValue()
        context?.theme?.resolveAttribute(R.attr.background, primaryColor, true)
        context?.theme?.resolveAttribute(R.attr.secondaryBackground, secondaryColor, true)

        var useSecondaryColor : Boolean = false
        filteredContacts.forEach {
            val contact = it
            val color = ColorDrawable(resources.getColor(
                    if(useSecondaryColor == true)
                        primaryColor.resourceId
                    else
                        secondaryColor.resourceId))

            val view = Contact.createView(inflater, contactsListView, it, color)
            view.setOnClickListener {
                view.isSelected = !view.isSelected
                contact.selected = view.isSelected
            }
            contactsListView.addView(view)
            useSecondaryColor = !useSecondaryColor
        }

        progressBar.visibility = View.GONE
    }
}