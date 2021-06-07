package com.example.orangemeet.ui.main.meetings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.orangemeet.*
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.User
import com.example.orangemeet.UserInfo
import com.example.orangemeet.ui.utils.MeetingUiUtils
import com.example.orangemeet.ui.utils.UserUiUtils
import com.example.orangemeet.utils.Util
import java.text.SimpleDateFormat

class MeetingsFragment : Fragment() {

    lateinit var meetingsViewModel: MeetingsViewModel

    lateinit var searchBar : SearchView
    lateinit var meetingsListView : LinearLayout
    lateinit var getMeetingsProgressBar : ProgressBar
    lateinit var addMeetingButton : MenuItem
    lateinit var meetingPopup : View
    lateinit var meetingPopupOwner : View
    lateinit var meetingPopupParticipants : LinearLayout
    lateinit var meetingPopupName : TextView
    lateinit var meetingPopupDateTime : TextView
    lateinit var meetingPopupButton: Button
    lateinit var notFoundPlaceholder : View

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
        val view = inflater.inflate(R.layout.fragment_meetings, container, false)
        setHasOptionsMenu(true)

        meetingsViewModel = ViewModelProvider(this).get(MeetingsViewModel::class.java)

        notFoundPlaceholder = view.findViewById(R.id.notFoundPlaceholder)

        meetingPopup = view.findViewById(R.id.meetingPopup)
        meetingPopupOwner = meetingPopup.findViewById(R.id.owner)
        meetingPopupParticipants = meetingPopup.findViewById(R.id.participantsList)
        meetingPopupName = meetingPopup.findViewById(R.id.meetingName)
        meetingPopupDateTime = meetingPopup.findViewById(R.id.meetingDateTime)
        meetingPopupButton = meetingPopup.findViewById(R.id.joinMeetingButton)
        meetingPopup.setOnClickListener { meetingPopup.visibility = View.GONE }

        meetingsListView = view.findViewById(R.id.meetingsList)
        getMeetingsProgressBar = view.findViewById(R.id.progressBar)
        searchBar = view.findViewById(R.id.searchView)

        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { return true }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    meetingsViewModel.updateQuery(newText)
                return true
            }
        })

        meetingsViewModel.displayedMeetings.observe(viewLifecycleOwner,
            Observer {meetings ->
                getMeetingsProgressBar.visibility = View.GONE
                displayMeetings(inflater, meetings)
            })

        meetingsViewModel.meetingsParticipants.observe(viewLifecycleOwner,
                Observer {participants ->
                    participants.forEach{participant ->
                        val contactView = UserUiUtils.createSmallView(inflater, meetingPopupParticipants, participant, null)
                        meetingPopupParticipants.addView(contactView)
                    }
                })

        meetingsViewModel.setOnErrorListener { error ->
            showError(error)
        }

        getMeetingsProgressBar.visibility = View.VISIBLE
        meetingsViewModel.getMeetings()

        return view
    }

    private fun showError(@StringRes error : Int){
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    private fun createMeetingItem(meeting : Meeting, inflater: LayoutInflater, evenView : Boolean) : View{
        val meetingItem = MeetingUiUtils.createView(
                inflater,
                meetingsListView,
                meeting,
                Util.createTintedBackground(requireContext(), evenView)
        )

        val joinMeetingButton = meetingItem.findViewById<ImageButton>(R.id.imageButton)

        joinMeetingButton.setOnClickListener {
            UserInfo.conferenceName = meeting.name.toString()
            UserInfo.conferenceId = meeting.id.toString()
            findNavController().navigate(R.id.nav_video)
        }

        meetingItem.setOnClickListener {
            val owner = meeting.owner
            val username = meetingPopupOwner.findViewById<TextView>(R.id.contactUsername)
            username.text = owner.username

            meetingPopupName.text = meeting.name
            meetingPopupDateTime.text = SimpleDateFormat("dd-MM-yyyy    kk:mm").format(meeting.date)

            meetingPopupButton.setOnClickListener {
                UserInfo.conferenceName = meeting.name.toString()
                UserInfo.conferenceId = meeting.id.toString()
                findNavController().navigate(R.id.nav_video)
            }

            meetingPopupParticipants.removeAllViews()
            meetingsViewModel.getMeetingParticipants(meeting)

            meetingPopup.visibility = View.VISIBLE
        }

        return meetingItem
    }

    private fun displayMeetings(inflater : LayoutInflater, meetings : List<Meeting>){
        meetingsListView.removeAllViews()

        notFoundPlaceholder.visibility = if (meetings.isEmpty()) View.VISIBLE else View.GONE

        var evenView = false
        meetings.forEach {meeting ->
            meetingsListView.addView(createMeetingItem(meeting, inflater, evenView))
            evenView = !evenView
        }
    }
}