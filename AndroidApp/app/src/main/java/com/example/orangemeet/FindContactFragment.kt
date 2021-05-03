package com.example.orangemeet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class FindContactFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView

    lateinit var contactsListView : LinearLayout

    lateinit var contacts : List<Contact>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val findContactFragment = inflater.inflate(R.layout.fragment_find_contact, container, false)

        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CreateContactViews(inflater)
                return true
            }
        })



        return findContactFragment
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

        filteredContacts.forEach {contact ->
            val view = Contact.createView(inflater, contactsListView, contact, null)
            contactsListView.addView(view)
        }

        progressBar.visibility = View.GONE
    }
}