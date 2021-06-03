package com.example.orangemeet.ui.main.meetings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.orangemeet.*
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.User
import com.example.orangemeet.UserInfo
import com.example.orangemeet.ui.utils.ResultInfo
import com.example.orangemeet.utils.Util
import java.text.SimpleDateFormat

class MeetingsFragment : Fragment() {

    lateinit var meetingsViewModel: MeetingsViewModel

    val meetings = MutableLiveData<MutableList<Meeting>?>()
    lateinit var searchBar : SearchView
    lateinit var meetingsListView : LinearLayout
    lateinit var progressBar : ProgressBar
    lateinit var addMeetingButton : MenuItem
    lateinit var meetingPopup : View
    lateinit var meetingPopupOwner : View
    lateinit var meetingPopupParticipants : LinearLayout
    lateinit var meetingPopupName : TextView
    lateinit var meetingPopupDateTime : TextView
    lateinit var meetingsFragment : View
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
        setHasOptionsMenu(true)

        meetingsViewModel = ViewModelProvider(this).get(MeetingsViewModel::class.java)

        meetingsFragment = inflater.inflate(R.layout.fragment_meetings, container, false)

        notFoundPlaceholder = meetingsFragment.findViewById(R.id.notFoundPlaceholder)
        meetingPopup = meetingsFragment.findViewById(R.id.meetingPopup)
        meetingPopupOwner = meetingPopup.findViewById(R.id.owner)
        meetingPopupParticipants = meetingPopup.findViewById(R.id.participantsList)
        meetingPopupName = meetingPopup.findViewById(R.id.meetingName)
        meetingPopupDateTime = meetingPopup.findViewById(R.id.meetingDateTime)
        meetingPopupButton = meetingPopup.findViewById(R.id.joinMeetingButton)
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

        meetingsViewModel.getMeetingsResult.observe(viewLifecycleOwner,
                Observer {getMeetingsResult ->
                    if(getMeetingsResult.success){
                        this.meetings.value = getMeetingsResult.data as MutableList<Meeting>?
                        progressBar.visibility = View.GONE
                    }else{
                        Toast.makeText(
                                requireContext(),
                                getString(getMeetingsResult.error!!),
                                Toast.LENGTH_LONG
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                })

        meetingsViewModel.getMeetings()
        /*BackendCommunication.getMeetings(
                BackendRequestQueue.getInstance(requireContext()).requestQueue,
            Response.Listener { meetings ->
                this.meetings.value = meetings.toMutableList()
                progressBar.visibility = View.GONE
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.get_meetings_fail),
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            })*/


        return meetingsFragment
    }

    private fun createMeetingViews(inflater : LayoutInflater, meetings : List<Meeting>?){
        if(meetings == null)
            return

        val filteredMeetings = meetings.filter { meeting ->
            if(searchBar.query.isEmpty())
                true
            else
                meeting.name.toLowerCase().contains(searchBar.query.toString().toLowerCase())
        }

        meetingsListView.removeAllViews()

        if(filteredMeetings.isEmpty())
            notFoundPlaceholder.visibility = View.VISIBLE
        else
            notFoundPlaceholder.visibility = View.GONE

        var evenView = false
        filteredMeetings.forEach {meeting ->

            val view = Meeting.createView(
                inflater,
                meetingsListView,
                meeting,
                Util.createTintedBackground(requireContext(), evenView)
            )

            val joinMeetingButton = view.findViewById<ImageButton>(R.id.imageButton)

            joinMeetingButton.setOnClickListener {
                UserInfo.conferenceName = meeting.name.toString()
                UserInfo.conferenceId = meeting.id.toString()
                findNavController().navigate(R.id.nav_video)
            }

            view.setOnClickListener {
                val owner = meeting.owner

                val username = meetingPopupOwner.findViewById<TextView>(R.id.contactUsername)

                username.text = owner.username

                meetingPopupName.text = meeting.name
                meetingPopupDateTime.text = SimpleDateFormat("dd-MM-yyyy    kk:mm").format(meeting.date)
                meetingPopupParticipants.removeAllViews()
                meetingPopupButton.setOnClickListener {
                    UserInfo.conferenceName = meeting.name.toString()
                    UserInfo.conferenceId = meeting.id.toString()
                    findNavController().navigate(R.id.nav_video)
                }

                var observerSet = false
                val observer = object : Observer<ResultInfo<List<User>>>{
                    override fun onChanged(result: ResultInfo<List<User>>?) {
                        if(!observerSet)
                            return
                        if(result!!.success){
                            result.data!!.forEach { contact ->
                                val contactView = User.createSmallView(inflater, meetingPopupParticipants, contact, null)
                                meetingPopupParticipants.addView(contactView)
                            }
                        }
                        meetingsViewModel.getMeetingParticipantsResult.removeObserver(this)
                    }
                }
                meetingsViewModel.getMeetingParticipantsResult.observe(viewLifecycleOwner, observer)
                observerSet = true
/*
                meetingViewModel.getMeetingsParticipantsResult.observe(viewLifecycleOwner,
                        Observer {result ->

                        })
                meetingViewModel.getMeetingsParticipantsResult.removeObservers(viewLifecycleOwner)*/

                meetingsViewModel.getMeetingParticipants(meeting)

                /*BackendCommunication.getMeetingParticipants(BackendRequestQueue.getInstance(requireContext()).requestQueue, meeting.id,
                    Response.Listener {contacts ->
                        contacts.forEach {contact ->
                            val contactView = User.createSmallView(inflater, meetingPopupParticipants, contact, null)
                            meetingPopupParticipants.addView(contactView)
                        }
                    },
                    Response.ErrorListener {

                    })*/

                meetingPopup.visibility = View.VISIBLE
            }

            meetingsListView.addView(view)
            evenView = !evenView
        }
    }
}