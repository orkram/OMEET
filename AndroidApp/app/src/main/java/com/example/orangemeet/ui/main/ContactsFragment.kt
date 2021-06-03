package com.example.orangemeet.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.example.orangemeet.*
import com.example.orangemeet.data.model.User
import com.example.orangemeet.services.BackendCommunication
import com.example.orangemeet.services.BackendRequestQueue
import com.example.orangemeet.utils.Util


class ContactsFragment : Fragment() {

    lateinit var contactsViewModel : ContactsViewModel

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView
    lateinit var notFoundPlaceholder : View

    lateinit var contactsListView : LinearLayout
    var contactsList = MutableLiveData<MutableList<User>>()
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

        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        val contactsFragment = inflater.inflate(R.layout.fragment_contacts, container, false)

        notFoundPlaceholder = contactsFragment.findViewById(R.id.notFoundPlaceholder)
        contactsListView = contactsFragment.findViewById(R.id.contactsLayout)
        progressBar = contactsFragment.findViewById(R.id.progressBar)
        searchBar = contactsFragment.findViewById(R.id.searchView)
        searchBar.setOnClickListener { v -> searchBar.isIconified = false }
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                createContactViews(inflater)
                return true
            }
        })

        contactsViewModel.deleteContactResult.observe(viewLifecycleOwner,
            Observer {deleteContactResult ->
                if(deleteContactResult.success){
                    contactsViewModel.getContacts()
                }else{
                    Toast.makeText(
                        requireContext(),
                        getString(deleteContactResult.error!!),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )

        contactsViewModel.getContactsResult.observe(viewLifecycleOwner,
            Observer {getContactsResult ->
                if(getContactsResult.success) {
                    contactsList.value = getContactsResult.data as MutableList<User>?
                }else{
                    Toast.makeText(context, getString(getContactsResult.error!!), Toast.LENGTH_LONG)
                            .show()
                    progressBar.visibility = View.GONE
                }
            })
        contactsList.observe(viewLifecycleOwner, Observer { createContactViews(inflater) })

        progressBar.visibility = View.VISIBLE
        contactsViewModel.getContacts()
        /*BackendCommunication.getContactsList(
            BackendRequestQueue.getInstance(requireContext()).requestQueue,
            Response.Listener { contactsList ->
                this.contactsList.value = contactsList.toMutableList()
                progressBar.visibility = View.GONE
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, getString(R.string.get_contacts_fail), Toast.LENGTH_LONG)
                    .show()
                progressBar.visibility = View.GONE
            })*/

        return contactsFragment
    }

    private fun createContactViews(inflater: LayoutInflater){
        if(contactsList.value == null)
            return

        val filteredContacts = contactsList.value!!.filter { contact ->
            if(searchBar.query.isEmpty())
                true
            else
                contact.username.toLowerCase().contains(searchBar.query.toString().toLowerCase())
        }

        contactsListView.removeAllViews()

        if(filteredContacts.isEmpty())
            notFoundPlaceholder.visibility = View.VISIBLE
        else
            notFoundPlaceholder.visibility = View.GONE

        var evenView = false
        filteredContacts.forEach {contact ->

            val view = User.createView(
                inflater,
                contactsListView,
                contact,
                Util.createTintedBackground(requireContext(), evenView)
            )

            val callButton = view.findViewById<ImageButton>(R.id.callButton)
            callButton.setOnClickListener {
                val includedContactBundle = Bundle();
                includedContactBundle.putString("username",  contact.username);
                findNavController().navigate(R.id.nav_create_meeting, includedContactBundle)
            }

            view.setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(resources.getString(R.string.delete_from_contacts)).setOnMenuItemClickListener {

                    AlertDialog.Builder(requireContext())
                            .setTitle(R.string.delete_from_contacts)
                            .setMessage(R.string.delete_contact_dialog_message)
                            .setPositiveButton(R.string.yes) { dialog, which ->
                                contactsViewModel.deleteContact(contact.username)
                               /* BackendCommunication.deleteContact(BackendRequestQueue.getInstance(requireContext()).requestQueue,
                                    contact.username,
                                    Response.Listener {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.contact_deleted_success),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        BackendCommunication.getContactsList(
                                                BackendRequestQueue.getInstance(requireContext()).requestQueue,
                                            Response.Listener {
                                                contactsList.value = it.toMutableList()
                                            },
                                            Response.ErrorListener {
                                                Toast.makeText(
                                                    requireContext(),
                                                    R.string.update_contacts_fail,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            })
                                    },
                                    Response.ErrorListener {
                                        Toast.makeText(
                                            requireContext(),
                                            R.string.contact_delete_fail,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    })*/
                            }
                        .setNegativeButton(R.string.no) { dialog, which -> }
                        .show()

                    true
                }
            }
            contactsListView.addView(view)
            evenView = !evenView
        }
    }
}