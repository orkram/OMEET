package com.example.orangemeet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

class MeetingsFragment : Fragment() {

    final var testMeetings = Array<Meeting>(20){i -> Meeting() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val meetingsFragment = inflater.inflate(R.layout.fragment_meetings, container, false)
        val meetingsListView = meetingsFragment.findViewById<LinearLayout>(R.id.meetingsList)

        var primaryColor = TypedValue()
        var secondaryColor = TypedValue()
        context?.theme?.resolveAttribute(R.attr.background, primaryColor, true)
        context?.theme?.resolveAttribute(R.attr.secondaryBackground, secondaryColor, true)

        var useSecondaryColor : Boolean = false
        testMeetings.forEach {
            val color = ColorDrawable(resources.getColor(
                    if(useSecondaryColor == true)
                        primaryColor.resourceId
                    else
                        secondaryColor.resourceId))

            val view = Meeting.createView(inflater, meetingsListView, it, color)

            meetingsListView.addView(view)
            useSecondaryColor = !useSecondaryColor
        }

        return meetingsFragment
    }
}