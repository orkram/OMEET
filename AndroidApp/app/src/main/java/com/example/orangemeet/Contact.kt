package com.example.orangemeet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.zip.Inflater
import kotlin.jvm.internal.Intrinsics


class Contact {
    lateinit var username : String
    lateinit var email : String
    lateinit var avatar : Image

    constructor(){
        username = "TestUsername"
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

        fun createView(inflater : LayoutInflater, root : ViewGroup,  contact: Contact, drawable: Drawable) : View{
            val view = inflater.inflate(R.layout.contacts_list_item, root, false)

            val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
            val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
            val box = view.findViewById<View>(R.id.box)
            userNameTextView.text = contact.username
            emailTextView.text = contact.email
            box.background = drawable
            System.out.println(userNameTextView.text.toString())
            return view
        }
    }

}