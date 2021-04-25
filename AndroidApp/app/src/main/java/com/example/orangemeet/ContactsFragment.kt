package com.example.orangemeet

import ContactsViewModel
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer


class ContactsFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : TextView

    val contactsViewModel = ContactsViewModel()


    lateinit var contactsListView : LinearLayout

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
            CreateContactViews(inflater, contactsViewModel.contactList.value!!)
        }

        val deleteButton = contactsFragment.findViewById<ImageButton>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Usuwanie kontaktów")
                .setMessage(R.string.delete_contacts_dialog)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        contactsViewModel.RemoveSelectedContactsFromBackend()
                        Toast.makeText(
                            context,
                            R.string.contacts_deleted_toast,
                            Toast.LENGTH_SHORT
                        ).show()
                        contactsViewModel.RemoveSelectedContactsFromBackend()
                    })
                .setNegativeButton(R.string.no, null).show()
        }

        contactsViewModel.contactList.observe(viewLifecycleOwner, Observer { contacts ->
            CreateContactViews(inflater, contacts)
        })

        progressBar.visibility = View.VISIBLE
        contactsViewModel.GetContactsFromBackend()

        return contactsFragment
    }

    private fun CreateContactViews(inflater: LayoutInflater, contacts : List<Contact>){
        val filteredContacts = contacts.filter { contact ->
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