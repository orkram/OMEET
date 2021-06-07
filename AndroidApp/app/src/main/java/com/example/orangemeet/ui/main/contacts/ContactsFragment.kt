package com.example.orangemeet.ui.main.contacts

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.orangemeet.*
import com.example.orangemeet.data.model.User
import com.example.orangemeet.ui.utils.UserUiUtils
import com.example.orangemeet.utils.Util


class ContactsFragment : Fragment() {

    lateinit var contactsViewModel : ContactsViewModel

    lateinit var loadContactsProgressBar : ProgressBar
    lateinit var searchBar : SearchView
    lateinit var notFoundPlaceholder : View
    lateinit var contactsListLayout : LinearLayout
    lateinit var addContactButton : MenuItem

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
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        setHasOptionsMenu(true)

        notFoundPlaceholder = view.findViewById(R.id.notFoundPlaceholder)
        contactsListLayout = view.findViewById(R.id.contactsLayout)
        loadContactsProgressBar = view.findViewById(R.id.progressBar)
        searchBar = view.findViewById(R.id.searchView)

        searchBar.setOnClickListener { v -> searchBar.isIconified = false }
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { return false }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    contactsViewModel.updateQuery(newText)
                return false
            }
        })

        contactsViewModel.displayedContacts.observe(viewLifecycleOwner,
            Observer {contacts ->
                displayContacts(contacts, inflater)
                loadContactsProgressBar.visibility = View.GONE
            })

        contactsViewModel.setOnErrorListener{
            error -> showError(error)
        }

        loadContactsProgressBar.visibility = View.VISIBLE
        contactsViewModel.refreshContacts()

        return view
    }

    private fun showError(@StringRes error : Int){
        Toast.makeText(context, getString(error), Toast.LENGTH_LONG).show()
    }

    private fun goCreateMeetingWithUser(contact: User){
        val includedContactBundle = Bundle();
        includedContactBundle.putParcelable("user",  contact);
        findNavController().navigate(R.id.nav_create_meeting, includedContactBundle)
    }

    private fun createContactItem(contact : User, inflater: LayoutInflater, evenView : Boolean) : View{

        val contactView = UserUiUtils.createView(
                inflater,
                contactsListLayout,
                contact,
                Util.createTintedBackground(requireContext(), evenView)
        )

        val callButton = contactView.findViewById<ImageButton>(R.id.callButton)

        callButton.setOnClickListener {
            goCreateMeetingWithUser(contact)
        }

        contactView.setOnLongClickListener {
            AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_from_contacts)
                    .setMessage(R.string.delete_contact_dialog_message)
                    .setPositiveButton(R.string.yes) { dialog, which ->
                        contactsViewModel.deleteContact(contact.username)
                    }
                    .setNegativeButton(R.string.no) { dialog, which -> }
                    .show()

            true
        }

        return contactView
    }

    private fun displayContacts(contacts : List<User>, inflater: LayoutInflater){
        contactsListLayout.removeAllViews()

        notFoundPlaceholder.visibility = if (contacts.isEmpty()) View.VISIBLE else View.GONE

        var evenView = false
        contacts.forEach {contact ->
            val contactView = createContactItem(contact, inflater, evenView)
            contactsListLayout.addView(contactView)
            evenView = !evenView
        }
    }
}