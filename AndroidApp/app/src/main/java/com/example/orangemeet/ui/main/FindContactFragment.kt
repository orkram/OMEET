package com.example.orangemeet.ui.main

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orangemeet.*
import com.example.orangemeet.data.model.User
import com.example.orangemeet.UserInfo
import com.example.orangemeet.utils.Util


class FindContactFragment : Fragment() {

    lateinit var findContactViewModel: FindContactViewModel

    lateinit var progressBar : ProgressBar
    lateinit var searchBar : SearchView

    lateinit var contactsListView : LinearLayout

    lateinit var searchPlaceholder : View
    lateinit var notFoundPlaceholder : View

    var users : List<User>? = null
    var contacts : List<User>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val findContactFragment = inflater.inflate(R.layout.fragment_find_contact, container, false)

        findContactViewModel = ViewModelProvider(this).get(FindContactViewModel::class.java)

        contactsListView = findContactFragment.findViewById(R.id.meetingsList)
        searchPlaceholder = findContactFragment.findViewById(R.id.searchPlaceholder)
        notFoundPlaceholder = findContactFragment.findViewById(R.id.notFoundPlaceholder)
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
                    notFoundPlaceholder.visibility = View.GONE
                }

                return false
            }
        })

        progressBar.visibility = View.VISIBLE
        searchPlaceholder.visibility = View.GONE

        findContactViewModel.getUsersResult.observe(viewLifecycleOwner,
            Observer {getUsersResult ->
                if(getUsersResult.success){
                    users = getUsersResult.data
                    findContactViewModel.getContacts()
                }else{
                    Toast.makeText(
                        requireContext(),
                        getString(getUsersResult.error!!),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        findContactViewModel.getContactsResult.observe(viewLifecycleOwner,
            Observer {getContactsResult ->
                if(getContactsResult.success){
                    contacts = getContactsResult.data
                    createContactViews(inflater)
                }else{
                    Toast.makeText(
                        requireContext(),
                        getString(getContactsResult.error!!),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        findContactViewModel.getUsers()

        /*BackendCommunication.getUsers(BackendRequestQueue.getInstance(requireContext()).requestQueue,
            null,
            Response.Listener { users ->
                this.users = users
                BackendCommunication.getContactsList(BackendRequestQueue.getInstance(requireContext()).requestQueue,
                Response.Listener { contacts ->
                    this.contacts = contacts;
                    progressBar.visibility = View.GONE
                    searchPlaceholder.visibility = View.VISIBLE
                },
                Response.ErrorListener {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.get_contacts_fail),
                            Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                })
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.get_users_fail),
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            })*/

        return findContactFragment
    }

    private fun createContactViews(inflater: LayoutInflater){
        if(users == null || contacts == null)
            return;

        val filteredContacts = users!!.filter { user ->
/*            if(searchBar.query.isEmpty())
                true
            else*/
                user.username.toLowerCase().contains(searchBar.query.toString().toLowerCase())
                        && contacts!!.find{ contact -> contact.username == user.username } == null
                        && user.username != UserInfo.userName
        }

        contactsListView.removeAllViews()

        if(filteredContacts.isEmpty()){
            notFoundPlaceholder.visibility = View.VISIBLE
        } else
            notFoundPlaceholder.visibility = View.GONE

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
                findContactViewModel.addContactResult.observe(viewLifecycleOwner,
                    Observer {addContactResult ->
                        findContactViewModel.addContactResult.removeObservers(viewLifecycleOwner)

                        if(addContactResult.success){
                            inviteButton.visibility = View.GONE
                            sentText.visibility = View.VISIBLE
                        }else{
                            Toast.makeText(
                                requireContext(),
                                getString(addContactResult.error!!),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                findContactViewModel.addContact(contact.username)
                /*BackendCommunication.addContact(BackendRequestQueue.getInstance(requireContext()).requestQueue,
                    contact.username,
                    Response.Listener {
                        inviteButton.visibility = View.GONE
                        sentText.visibility = View.VISIBLE
                    },
                    Response.ErrorListener {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.add_contact_fail),
                            Toast.LENGTH_LONG
                        ).show()
                    })*/
            }
            contactsListView.addView(view)
            evenView = !evenView
        }

    }
}