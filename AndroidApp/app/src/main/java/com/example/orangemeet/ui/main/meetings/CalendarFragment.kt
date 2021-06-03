package com.example.orangemeet.ui.main.meetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.orangemeet.*
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.UserInfo
import com.example.orangemeet.utils.Util
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {

    lateinit var calendarViewModel : CalendarViewModel

    lateinit var calendarView : CalendarView
    lateinit var meetingsView : ViewGroup
    lateinit var meetingDateView : TextView
    lateinit var noMeetingsPlaceholder : View

    var meetings : List<Meeting>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        calendarView = view.findViewById(R.id.calendarView)
        meetingsView = view.findViewById(R.id.meetingsView)
        meetingDateView = view.findViewById(R.id.meetingDate)
        noMeetingsPlaceholder = view.findViewById(R.id.noMeetingsPlaceholder)

        val events: MutableList<EventDay> = ArrayList()

        calendarViewModel.getMeetingsResult.observe(viewLifecycleOwner,
        androidx.lifecycle.Observer {result ->
            if(result.success){
                this.meetings = result.data
                this.meetings!!.forEach { meeting ->
                    val calendar = Calendar.getInstance()
                    calendar.time = meeting.date
                    events.add(EventDay(calendar, R.drawable.main_meeting))
                    meetingDateView.setText(SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().time))
                    CreateMeetingViews(Calendar.getInstance(), inflater)
                }
                calendarView.setEvents(events)
            }else{
                Toast.makeText(requireContext(), R.string.get_meetings_fail, Toast.LENGTH_LONG).show()
            }
        })

        calendarViewModel.getMeetings()

        /*BackendCommunication.getMeetings(BackendRequestQueue.getInstance(requireContext()).requestQueue,
            Response.Listener { meetings ->
                this.meetings = meetings
                this.meetings!!.forEach { meeting ->
                    val calendar = Calendar.getInstance()
                    calendar.time = meeting.date
                    events.add(EventDay(calendar, R.drawable.main_meeting))
                    meetingDateView.setText(SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().time))
                    CreateMeetingViews(Calendar.getInstance(), inflater)
                }
                calendarView.setEvents(events)
            },
            Response.ErrorListener {
                Toast.makeText(requireContext(), "Nie udało się załadować spotkań", Toast.LENGTH_LONG).show()
            })*/

        calendarView.setOnDayClickListener {eventDay ->
            val clickedDayCalendar = eventDay.calendar
            if(calendarView.currentPageDate.get(Calendar.MONTH) ==
                Calendar.getInstance().apply { time = clickedDayCalendar.time }.get(Calendar.MONTH) )
            {
                meetingDateView.setText(SimpleDateFormat("dd-MM-yy").format(clickedDayCalendar.time))
                CreateMeetingViews(clickedDayCalendar, inflater)
            }
        }

        return view
    }

    private fun CreateMeetingViews(clickedDayCalendar : Calendar, inflater: LayoutInflater){
        meetingsView.removeAllViews()

        val filteredMeetings = meetings!!.filter { meeting ->
            val meetingCalendar = Calendar.getInstance().apply { time = meeting.date }
            meetingCalendar.get(Calendar.DAY_OF_YEAR) == clickedDayCalendar.get(Calendar.DAY_OF_YEAR) &&
                    meetingCalendar.get(Calendar.YEAR) == clickedDayCalendar.get(Calendar.YEAR)
        }

        if(filteredMeetings.isEmpty()){
            noMeetingsPlaceholder.visibility = View.VISIBLE
        }else{
            noMeetingsPlaceholder.visibility = View.GONE

            var evenView = false
            filteredMeetings?.forEach {meeting ->
                val meetingView = Meeting.createView(inflater, meetingsView, meeting, Util.createTintedBackground(requireContext(), evenView))

                meetingView.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
                    UserInfo.conferenceName = meeting.name.toString()
                    UserInfo.conferenceId = meeting.id.toString()
                    findNavController().navigate(R.id.nav_video)
                }

                meetingsView.addView(meetingView)
                evenView = !evenView
            }
        }

    }

    companion object {

        fun newInstance(param1: String, param2: String) =
                CalendarFragment()
    }
}