package com.example.orangemeet.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.Meeting
import com.example.orangemeet.R
import com.example.orangemeet.User

class MeetingsFragment : Fragment() {

    var testMeetings = Array(20){ i -> Meeting() }

    val meetings = MutableLiveData<MutableList<Meeting>?>()
    lateinit var searchBar : SearchView
    lateinit var meetingsListView : LinearLayout
    lateinit var progressBar : ProgressBar
    lateinit var addMeetingButton : MenuItem
    lateinit var meetingPopup : View
    lateinit var meetingPopupOwner : View
    lateinit var meetingPopupParticipants : LinearLayout
    lateinit var meetingPopupName : TextView
    lateinit var meetingsFragment : View

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meetings, menu)

        addMeetingButton = menu.findItem(R.id.addMeeting)

        addMeetingButton.setOnMenuItemClickListener {
            findNavController().navigate(R.id.nav_create_meeting)
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        meetingsFragment = inflater.inflate(R.layout.fragment_meetings, container, false)

        meetingPopup = meetingsFragment.findViewById(R.id.meetingPopup)
        meetingPopupOwner = meetingPopup.findViewById(R.id.owner)
        meetingPopupParticipants = meetingPopup.findViewById(R.id.participantsList)
        meetingPopupName = meetingPopup.findViewById(R.id.meetingName)
        meetingPopup.setOnClickListener { meetingPopup.visibility = View.GONE }

        meetingsListView = meetingsFragment.findViewById(R.id.meetingsList)
        progressBar = meetingsFragment.findViewById(R.id.progressBar)
        searchBar = meetingsFragment.findViewById(R.id.searchView)
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                createMeetingViews(inflater, meetings.value)
                return true
            }
        })

        meetings.value = null
        meetings.observe(viewLifecycleOwner, Observer { createMeetingViews(inflater, meetings.value) })

        progressBar.visibility = View.VISIBLE
        BackendCommunication.getMeetings(
            requireContext(),
            Response.Listener { meetings ->
                createMeetingViews(inflater, meetings)
                progressBar.visibility = View.GONE
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    "Nie udało się pobrać listy spotkań",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            })


        return meetingsFragment
    }

    private fun createMeetingViews(inflater : LayoutInflater, meetings : List<Meeting>?){
        if(meetings == null)
            return

        val filteredMeetings = meetings.filter { meeting ->
            if(searchBar.query.isEmpty())
                true
            else
                meeting.name.contains(searchBar.query.toString().toLowerCase())
        }

        meetingsListView.removeAllViews()

        filteredMeetings.forEach {meeting ->
            val view = Meeting.createView(
                inflater,
                meetingsListView,
                meeting,
                null
            )

            view.setOnClickListener {
                val owner = meeting.owner

                val username = meetingPopupOwner.findViewById<TextView>(R.id.contactUsername)

                username.text = owner.username

                meetingPopupName.text = meeting.name

                meetingPopupParticipants.removeAllViews()

                BackendCommunication.getContactsList(requireContext(),
                    Response.Listener {contacts ->
                        contacts.forEach {contact ->
                            val contactView = User.createSmallView(inflater, meetingPopupParticipants, contact, null)
                            meetingPopupParticipants.addView(contactView)
                        }
                    },
                    Response.ErrorListener {

                    })

                meetingPopup.visibility = View.VISIBLE
            }

            meetingsListView.addView(view)
        }
    }
}