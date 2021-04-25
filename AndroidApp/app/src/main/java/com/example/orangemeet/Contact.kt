package com.example.orangemeet

import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*


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
            val contact = Contact()
            return contact
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, contact: Contact, background: Drawable) : View{
            val view = inflater.inflate(R.layout.contacts_list_item, root, false)

            val userNameTextView = view.findViewById<TextView>(R.id.meetingId)
            val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
            val box = view.findViewById<View>(R.id.box)
            userNameTextView.text = contact.username
            emailTextView.text = contact.email
            box.background = background
            System.out.println(userNameTextView.text.toString())
            return view
        }
    }

}