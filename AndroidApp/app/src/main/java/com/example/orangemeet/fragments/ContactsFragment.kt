package com.example.orangemeet.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.R
import com.example.orangemeet.User


class ContactsFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView


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

        val contactsFragment = inflater.inflate(R.layout.fragment_contacts, container, false)
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

        contactsList.observe(viewLifecycleOwner, Observer { createContactViews(inflater) })

        progressBar.visibility = View.VISIBLE
        BackendCommunication.getContactsList(
            requireContext(),
            Response.Listener { contactsList ->
                this.contactsList.value = contactsList.toMutableList()
                progressBar.visibility = View.GONE
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Nie udało się pobrać listy kontaktów", Toast.LENGTH_LONG)
                    .show()
                progressBar.visibility = View.GONE
            })

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

        filteredContacts.forEach {contact ->
            val view = User.createView(
                inflater,
                contactsListView,
                contact,
                null
            )
            view.setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(resources.getString(R.string.delete_from_contacts)).setOnMenuItemClickListener {

                    AlertDialog.Builder(requireContext())
                            .setTitle(R.string.delete_from_contacts)
                            .setMessage(R.string.delete_contact_dialog_message)
                            .setPositiveButton(R.string.yes) { dialog, which ->
                                BackendCommunication.deleteContact(requireContext(),
                                    contact.username,
                                    Response.Listener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Pomyślnie usunięto kontakt",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        BackendCommunication.getContactsList(
                                            requireContext(),
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
                                    })
                            }
                        .setNegativeButton(R.string.no) { dialog, which -> }
                        .show()

                    true
                }
            }
            contactsListView.addView(view)
        }
    }
}