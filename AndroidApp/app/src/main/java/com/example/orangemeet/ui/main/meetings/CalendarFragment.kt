//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.main.meetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.orangemeet.*
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.UserInfo
import com.example.orangemeet.ui.utils.MeetingUiUtils
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

        calendarViewModel.getMeetingsResult.observe(viewLifecycleOwner,
        androidx.lifecycle.Observer {result ->
            if(result.success){
                setEvents(result.data!!)
                val today = Calendar.getInstance().time
                meetingDateView.text = SimpleDateFormat("dd-MM-yy").format(today)
                calendarViewModel.selectDay(today)
            }else{
                showError(R.string.get_meetings_fail)
            }
        })

        calendarViewModel.selectedDayMeetings.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {meetings ->
                displayMeetings(meetings)
            })

        calendarView.setOnDayClickListener {eventDay ->
            val clickedDayCalendar = eventDay.calendar
            if(calendarView.currentPageDate.get(Calendar.MONTH) ==
                Calendar.getInstance().apply { time = clickedDayCalendar.time }.get(Calendar.MONTH) )
            {
                meetingDateView.text = SimpleDateFormat("dd-MM-yy").format(clickedDayCalendar.time)
                calendarViewModel.selectDay(clickedDayCalendar.time)
            }
        }

        calendarViewModel.getMeetings()

        return view
    }

    private fun setEvents(meetings : List<Meeting>) {
        val events: MutableList<EventDay> = ArrayList()
        meetings.forEach { meeting ->
            val calendar = Calendar.getInstance()
            calendar.time = meeting.date
            events.add(EventDay(calendar, R.drawable.main_meeting))
        }
        calendarView.setEvents(events)
    }

    private fun showError(@StringRes error : Int) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    private fun createMeetingItem(meeting: Meeting, evenView : Boolean) : View {
        val meetingItem = MeetingUiUtils.createView(layoutInflater, meetingsView, meeting, Util.createTintedBackground(requireContext(), evenView))

        meetingItem.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            UserInfo.conferenceName = meeting.name.toString()
            UserInfo.conferenceId = meeting.id.toString()
            findNavController().navigate(R.id.nav_video)
        }

        return meetingItem
    }

    private fun displayMeetings(meetings : List<Meeting>){
        meetingsView.removeAllViews()

        if(meetings.isEmpty()){
            noMeetingsPlaceholder.visibility = View.VISIBLE
        }else{
            noMeetingsPlaceholder.visibility = View.GONE

            var evenView = false
            meetings.forEach {meeting ->
                meetingsView.addView(createMeetingItem(meeting, evenView))
                evenView = !evenView
            }
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
                CalendarFragment()
    }
}