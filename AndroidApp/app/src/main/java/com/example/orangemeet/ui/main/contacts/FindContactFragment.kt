//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.main.contacts

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orangemeet.*
import com.example.orangemeet.data.model.User
import com.example.orangemeet.data.model.UserAvatarPair
import com.example.orangemeet.ui.utils.UserUiUtils
import com.example.orangemeet.utils.Util


class FindContactFragment : Fragment() {

    lateinit var findContactViewModel: FindContactViewModel

    lateinit var loadUsersProgressBar : ProgressBar
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
        val view = inflater.inflate(R.layout.fragment_find_contact, container, false)

        findContactViewModel = ViewModelProvider(this).get(FindContactViewModel::class.java)

        contactsListView = view.findViewById(R.id.meetingsList)
        searchPlaceholder = view.findViewById(R.id.searchPlaceholder)
        notFoundPlaceholder = view.findViewById(R.id.notFoundPlaceholder)
        loadUsersProgressBar = view.findViewById(R.id.progressBar)
        searchBar = view.findViewById(R.id.searchView)

        searchPlaceholder.visibility = View.GONE

        searchBar.setOnClickListener { v -> searchBar.isIconified = false }
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { return false }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    findContactViewModel.updateQuery(newText!!)
                }
                return false
            }
        })

        findContactViewModel.displayedUsers.observe(viewLifecycleOwner,
            Observer {displayedUsers ->
                displayUsers(displayedUsers, inflater)
                loadUsersProgressBar.visibility = View.GONE
            })

        findContactViewModel.setOnErrorListener {error ->
            showError(error)
        }

        loadUsersProgressBar.visibility = View.VISIBLE
        findContactViewModel.refreshUsers()

        return view
    }

    private fun showError(@StringRes error : Int){
        Toast.makeText(
                requireContext(),
                getString(error),
                Toast.LENGTH_LONG
        ).show()
    }

    private fun createUserItem(userAvatarPair: UserAvatarPair, inflater: LayoutInflater, evenView : Boolean) : View{
        val userItem = UserUiUtils.createInviteView(
                inflater,
                contactsListView,
                userAvatarPair.user,
                Util.createTintedBackground(requireContext(), evenView),
                userAvatarPair.avatar
        )

        val inviteButton = userItem.findViewById<Button>(R.id.inviteButton)
        val sentText = userItem.findViewById<TextView>(R.id.sentText)

        inviteButton.setOnClickListener {
            findContactViewModel.addContact(userAvatarPair.user.username){ result ->
                if(result.success){
                    inviteButton.visibility = View.GONE
                    sentText.visibility = View.VISIBLE
                }else{
                    showError(result.error!!)
                }
            }
        }

        return userItem
    }

    private fun displayUsers(users : List<UserAvatarPair>, inflater: LayoutInflater){
        contactsListView.removeAllViews()

        notFoundPlaceholder.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE

        var evenView = false
        users.forEach {userAvatarPair ->
            val userItem = createUserItem(userAvatarPair, inflater, evenView)
            contactsListView.addView(userItem)
            evenView = !evenView
        }

    }
}