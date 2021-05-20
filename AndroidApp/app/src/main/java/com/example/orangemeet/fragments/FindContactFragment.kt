package com.example.orangemeet.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.R
import com.example.orangemeet.User
import com.example.orangemeet.Util


class FindContactFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView

    lateinit var contactsListView : LinearLayout

    lateinit var searchPlaceholder : View

    var users : List<User>? = null
    var contacts : List<User>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val findContactFragment = inflater.inflate(R.layout.fragment_find_contact, container, false)
        contactsListView = findContactFragment.findViewById(R.id.meetingsList)
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
                    createContactViews(inflater)
                    searchPlaceholder.visibility = View.GONE
                } else{
                    contactsListView.removeAllViews()
                    searchPlaceholder.visibility = View.VISIBLE
                }

                return false
            }
        })

        progressBar.visibility = View.VISIBLE
        searchPlaceholder.visibility = View.GONE

        BackendCommunication.getUsers(requireContext(),
            null,
            Response.Listener { users ->
                this.users = users
                BackendCommunication.getContactsList(requireContext(),
                Response.Listener { contacts ->
                    this.contacts = contacts;
                    progressBar.visibility = View.GONE
                    searchPlaceholder.visibility = View.VISIBLE
                },
                Response.ErrorListener {
                    Toast.makeText(
                            requireContext(),
                            "Nie udało się pobrać listy znajomych",
                            Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                })
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    "Nie udało się pobrać listy użytkowników",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            })

        return findContactFragment
    }

    private fun createContactViews(inflater: LayoutInflater){
        if(users == null || contacts == null)
            return;

        val filteredContacts = users!!.filter { user ->
            if(searchBar.query.isEmpty())
                true
            else
                user.username.toLowerCase().contains(searchBar.query.toString().toLowerCase())
                        && contacts!!.find{ contact -> contact.equals(user) } == null
        }

        contactsListView.removeAllViews()

        var evenView = false
        filteredContacts.forEach {contact ->
            val view = User.createInviteView(
                inflater,
                contactsListView,
                contact,
                Util.createTintedBackground(requireContext(), evenView)
            )
            val inviteButton = view.findViewById<Button>(R.id.inviteButton)
            val sentText = view.findViewById<TextView>(R.id.sentText)
            inviteButton.setOnClickListener {
                BackendCommunication.addContact(requireContext(),
                    contact.username,
                    Response.Listener {
                        inviteButton.visibility = View.GONE
                        sentText.visibility = View.VISIBLE
                    },
                    Response.ErrorListener {
                        Toast.makeText(
                            requireContext(),
                            "Nie udało się dodać do znajomych",
                            Toast.LENGTH_LONG
                        ).show()
                    })
            }
            contactsListView.addView(view)
            evenView = !evenView
        }

    }
}