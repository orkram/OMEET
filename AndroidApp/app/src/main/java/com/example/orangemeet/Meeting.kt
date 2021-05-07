package com.example.orangemeet

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class Meeting {
    var id : Int = -1
    lateinit var name : String
    lateinit var participants : List<Contact>
    lateinit var owner : Contact
    lateinit var date: Date
    lateinit var roomUrl : String
    var isActive : Boolean = false


    constructor(){
        id = Random().nextInt(1000)
        name = "Name" + Util.GenerateRandomString(8)
        owner = Contact()
        participants = List<Contact>(3){ i -> Contact()}
        date = Date()
    }

    constructor(id : Int, name : String, date : Date, owner : Contact, roomUrl : String){
        this.name = name
        this.id = id
        this.date = date
        this.owner = owner
        this.roomUrl = roomUrl
    }

    constructor(isActive : Boolean) : super() {
        this.isActive = isActive
    }

    companion object{
        fun createFromJson(jsonObject: JSONObject) : Meeting{
            return Meeting()
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, meeting: Meeting, background: Drawable?) : View {
            val view = inflater.inflate(R.layout.meetings_list_item, root, false)

            val idTextView = view.findViewById<TextView>(R.id.meetingName)
            val dateTextView = view.findViewById<TextView>(R.id.date)
            val ownerTextView = view.findViewById<TextView>(R.id.meetingOwner)
            val nameTextView = view.findViewById<TextView>(R.id.meetingName)
            val box = view.findViewById<View>(R.id.box)
            idTextView.text = meeting.id.toString()
            dateTextView.text = SimpleDateFormat("dd-MM-yyyy").format(meeting.date)
            ownerTextView.text = meeting.owner.username
            nameTextView.text = meeting.name
            if(background != null)
                box.background = background
            return view
        }
    }
}