package com.example.orangemeet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response


class FindContactFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView

    lateinit var contactsListView : LinearLayout

    lateinit var searchPlaceholder : View

    var contacts : List<Contact>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val findContactFragment = inflater.inflate(R.layout.fragment_find_contact, container, false)
        contactsListView = findContactFragment.findViewById<LinearLayout>(R.id.contactsList)
        searchPlaceholder = findContactFragment.findViewById(R.id.searchPlaceholder)
        progressBar = findContactFragment.findViewById(R.id.progressBar)
        searchBar = findContactFragment.findViewById(R.id.searchView)
        searchBar.setOnClickListener { v -> searchBar.isIconified = false }
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.length >= 3) {
                    CreateContactViews(inflater)
                    searchPlaceholder.visibility = View.GONE
                } else{
                    contactsListView.removeAllViews()
                    searchPlaceholder.visibility = View.VISIBLE
                }

                return false
            }
        })

        //TODO: Remove test contacts
        contacts = MutableList<Contact>(40){i -> Contact()}
        //CreateContactViews(inflater)
        /*BackendCommunication.GetUsers(requireContext(), null,
        Response.Listener {contacts ->
            this.contacts = contacts
            CreateContactViews(inflater)
        },
        Response.ErrorListener {

        })*/

        return findContactFragment
    }

    private fun CreateContactViews(inflater: LayoutInflater){
        val filteredContacts = contacts!!.filter { contact ->
            if(searchBar.query.isEmpty())
                true
            else
                contact.username.toLowerCase().contains(searchBar.query.toString().toLowerCase())
        }

        contactsListView.removeAllViews()

        filteredContacts.forEach {contact ->
            val view = Contact.createInviteView(inflater, contactsListView, contact, null)
            val inviteButton = view.findViewById<Button>(R.id.inviteButton)
            val sentText = view.findViewById<TextView>(R.id.sentText)
            inviteButton.setOnClickListener {
                inviteButton.visibility = View.GONE
                sentText.visibility = View.VISIBLE
            }
            contactsListView.addView(view)
        }

        progressBar.visibility = View.GONE
    }
}