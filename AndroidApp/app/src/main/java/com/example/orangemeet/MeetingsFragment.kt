package com.example.orangemeet

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import java.util.*

class MeetingsFragment : Fragment() {

    final var testMeetings = Array<Meeting>(20){i -> Meeting() }

    val meetings = MutableLiveData<MutableList<Meeting>?>()
    lateinit var searchBar : SearchView
    lateinit var meetingsListView : LinearLayout
    lateinit var progressBar : ProgressBar
    lateinit var addMeetingButton : MenuItem

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

        val meetingsFragment = inflater.inflate(R.layout.fragment_meetings, container, false)
        meetingsListView = meetingsFragment.findViewById<LinearLayout>(R.id.meetingsList)
        progressBar = meetingsFragment.findViewById(R.id.progressBar)
        searchBar = meetingsFragment.findViewById(R.id.searchView)
        searchBar.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CreateMeetingViews(inflater, meetings.value)
                return true
            }
        })

        meetings.value = null
        meetings.observe(viewLifecycleOwner, Observer { CreateMeetingViews(inflater, meetings.value) })

        progressBar.visibility = View.VISIBLE
        BackendCommunication.GetMeetings(requireContext(),
                Response.Listener {meetings ->
                    CreateMeetingViews(inflater, meetings)
                    progressBar.visibility = View.GONE
                },
                Response.ErrorListener {

                    progressBar.visibility = View.GONE
                })


        return meetingsFragment
    }

    private fun CreateMeetingViews(inflater : LayoutInflater, meetings : List<Meeting>?){
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
            val view = Meeting.createView(inflater, meetingsListView, meeting, null)

            view.setOnClickListener {
                val participantsView = it.findViewById<View>(R.id.participantsView)
                val participantsInnerView = participantsView.findViewById<TextView>(R.id.participantsInnerView)
                if(participantsView.visibility == View.GONE){
                    BackendCommunication.GetMeetingParticipants(requireContext(), meeting.id,
                            Response.Listener {contacts ->
                                participantsInnerView.text = ""
                                contacts.forEach{contact ->
                                    participantsInnerView.append(contact.username + "\n")
                                }
                            },
                            Response.ErrorListener {
                                Toast.makeText(requireContext(), "Nie udało się pobrać listy uczestników", Toast.LENGTH_LONG).show()
                            })
                    participantsView.visibility = View.VISIBLE
                } else{
                    participantsView.visibility = View.GONE
                }
            }

            meetingsListView.addView(view)
        }
    }
}