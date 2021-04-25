package com.example.orangemeet

import MeetingsViewModel
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.util.zip.Inflater

class MeetingsFragment : Fragment() {

    final var testMeetings = Array<Meeting>(20){i -> Meeting() }

    val meetingsViewModel = MeetingsViewModel()
    lateinit var searchBar : TextView
    lateinit var meetingsListView : LinearLayout
    lateinit var progressBar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val meetingsFragment = inflater.inflate(R.layout.fragment_meetings, container, false)
        meetingsListView = meetingsFragment.findViewById<LinearLayout>(R.id.meetingsList)
        progressBar = meetingsFragment.findViewById(R.id.progressBar)
        searchBar = meetingsFragment.findViewById(R.id.searchBar)
        searchBar.doOnTextChanged { text, start, before, count ->
            CreateMeetingViews(inflater, meetingsViewModel.meetingsList.value!!)
        }

        meetingsViewModel.meetingsList.observe(viewLifecycleOwner, Observer { contacts ->
            CreateMeetingViews(inflater, contacts)
        })

        progressBar.visibility = View.VISIBLE

        meetingsViewModel.GetContactsFromBackend()

        return meetingsFragment
    }

    private fun CreateMeetingViews(inflater : LayoutInflater, meetings : List<Meeting>){
        val filteredMeetings = meetings.filter { meeting ->
            if(searchBar.text.isEmpty())
                true
            else
                meeting.id.contains(searchBar.text.toString().toLowerCase())
        }

        meetingsListView.removeAllViews()

        var primaryColor = TypedValue()
        var secondaryColor = TypedValue()
        context?.theme?.resolveAttribute(R.attr.background, primaryColor, true)
        context?.theme?.resolveAttribute(R.attr.secondaryBackground, secondaryColor, true)

        var useSecondaryColor : Boolean = false
        filteredMeetings.forEach {
            val color = ColorDrawable(resources.getColor(
                    if(useSecondaryColor == true)
                        primaryColor.resourceId
                    else
                        secondaryColor.resourceId))

            val view = Meeting.createView(inflater, meetingsListView, it, color)

            meetingsListView.addView(view)
            useSecondaryColor = !useSecondaryColor
        }
    }
}