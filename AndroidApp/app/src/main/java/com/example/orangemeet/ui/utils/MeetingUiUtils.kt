//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.utils

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.orangemeet.R
import com.example.orangemeet.data.model.Meeting
import java.text.SimpleDateFormat

class MeetingUiUtils {
    companion object {
        
        fun createView(inflater : LayoutInflater, root : ViewGroup, meeting: Meeting, background: Drawable?) : View {
            val view = inflater.inflate(R.layout.item_meeting, root, false)

            val dateTextView = view.findViewById<TextView>(R.id.date)
            val ownerTextView = view.findViewById<TextView>(R.id.meetingOwner)
            val nameTextView = view.findViewById<TextView>(R.id.meetingName)
            dateTextView.text = SimpleDateFormat("dd-MM-yyyy    kk:mm").format(meeting.date)
            ownerTextView.text = meeting.owner.username
            nameTextView.text = meeting.name
            if(background != null) {
                view.background = background
            }
            return view
        }
    }
}