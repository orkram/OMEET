package com.example.orangemeet

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Meeting {
    lateinit var id : String
    lateinit var participants : List<Contact>
    lateinit var date: Date
    var isActive : Boolean = false


    constructor(){
        id = "TestId"
        participants = listOf<Contact>(Contact(), Contact(), Contact())
        date = Date()
    }

    constructor(isActive : Boolean) : super() {
        this.isActive = isActive
    }

    companion object{
        fun createFromJson(jsonObject: JSONObject) : Meeting{
            return Meeting()
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, meeting: Meeting, background: Drawable) : View {
            val view = inflater.inflate(R.layout.meetings_list_item, root, false)

            val idTextView = view.findViewById<TextView>(R.id.meetingId)
            val dateTextView = view.findViewById<TextView>(R.id.date)
            val box = view.findViewById<View>(R.id.box)
            idTextView.text = meeting.id
            dateTextView.text = SimpleDateFormat("dd-MM-yyyy").format(meeting.date)
            box.background = background
            return view
        }
    }
}