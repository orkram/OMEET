package com.example.orangemeet

import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject


class Contact {
    lateinit var username : String
    lateinit var email : String
    lateinit var avatar : Image
    var selected = false

    constructor(){
        username = "TestUsername" + Util.GenerateRandomString(8)
        email = "Test@Email.com"
    }

    constructor(username : String, email : String){
        this.username = username
        this.email = email
    }

    companion object{
        fun createFromJson(jsonObject: JSONObject) : Contact{
            val str = jsonObject.toString()
            val username = jsonObject.getString("userName")
            val email = jsonObject.getString("eMail")
            val contact = Contact(username, email)
            return contact
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, contact: Contact, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.contacts_list_item, root, false)

            val userNameTextView = view.findViewById<TextView>(R.id.meetingId)
            val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
            val box = view.findViewById<View>(R.id.box)
            userNameTextView.text = contact.username
            emailTextView.text = contact.email
            box.background = background
            return view
        }

        fun createInviteView(inflater : LayoutInflater, root : ViewGroup, contact: Contact, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.contacts_list_item_invite, root, false)

            val userNameTextView = view.findViewById<TextView>(R.id.meetingId)
            val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
            val box = view.findViewById<View>(R.id.box)
            userNameTextView.text = contact.username
            emailTextView.text = contact.email
            box.background = background
            return view
        }
    }

}