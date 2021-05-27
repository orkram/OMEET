package com.example.orangemeet.fragments

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.Meeting
import com.example.orangemeet.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {

    lateinit var calendarView : CalendarView
    lateinit var meetingsView : ViewGroup
    lateinit var meetingDateView : TextView

    var meetings : List<Meeting>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        meetingsView = view.findViewById(R.id.meetingsView)
        meetingDateView = view.findViewById(R.id.meetingDate)

        meetingDateView.setText(SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().time))
        CreateMeetingViews(Calendar.getInstance(), inflater)

        val events: MutableList<EventDay> = ArrayList()

        BackendCommunication.getMeetings(requireContext(),
            Response.Listener { meetings ->
                this.meetings = meetings
                this.meetings!!.forEach { meeting ->
                    val calendar = Calendar.getInstance()
                    calendar.time = meeting.date
                    events.add(EventDay(calendar, R.drawable.baseline_account_circle_24))
                }
                calendarView.setEvents(events)
            },
            Response.ErrorListener {
                Toast.makeText(requireContext(), "Nie udało się załadować spotkań", Toast.LENGTH_LONG).show()
            })

        calendarView.setOnDayClickListener {eventDay ->
            val clickedDayCalendar = eventDay.calendar

            meetingDateView.setText(SimpleDateFormat("dd-MM-yy").format(clickedDayCalendar.time))

            meetingsView.removeAllViews()

            CreateMeetingViews(clickedDayCalendar, inflater)
        }

        return view
    }

    private fun CreateMeetingViews(clickedDayCalendar : Calendar, inflater: LayoutInflater){
        meetings?.forEach {meeting ->
            val meetingCalendar = Calendar.getInstance().apply { time = meeting.date }
            if(meetingCalendar.get(Calendar.DAY_OF_YEAR) == clickedDayCalendar.get(Calendar.DAY_OF_YEAR) &&
                meetingCalendar.get(Calendar.YEAR) == clickedDayCalendar.get(Calendar.YEAR)){

                meetingsView.addView(Meeting.createView(inflater, meetingsView, meeting, null))
            }
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            CalendarFragment()
    }
}