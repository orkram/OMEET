package com.example.orangemeet.data.model

import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.orangemeet.R
import com.example.orangemeet.utils.Util
import org.json.JSONObject


class User {
    lateinit var username : String
    lateinit var email : String
    lateinit var avatar : Image
    var selected = false

    constructor(){
        username = "TestUsername" + Util.generateRandomString(8)
        email = "Test@Email.com"
    }

    constructor(username : String, email : String){
        this.username = username
        this.email = email
    }

    override fun equals(other: Any?): Boolean {
        if(other !is User)
            return false;
        else{
            return (other as User).username == this.username
        }
    }

    companion object{
        fun createFromJson(jsonObject: JSONObject) : User {
            val str = jsonObject.toString()
            val username = jsonObject.getString("userName")
            val email = jsonObject.getString("eMail")
            return User(username, email)
        }

        private fun populateView(view : View, contact : User, background: Drawable?) : View{
            val userNameTextView = view.findViewById<TextView>(R.id.contactUsername)
            val emailTextView = view.findViewById<TextView>(R.id.contactEmail)
            val box = view.findViewById<View>(R.id.box)
            userNameTextView?.text = contact.username
            emailTextView?.text = contact.email
            if(background != null)
                box.background = background
            return view
        }

        fun createSmallView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.contact_details_small, root, false)
            view.findViewById<TextView>(R.id.contactUsername).text = contact.username
            return view
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.contact_item, root, false)
            return populateView(view, contact, background)
        }

        fun createInviteView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.contact_item_invite, root, false)
            return populateView(view, contact, background)
        }

        fun createCheckView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.contact_item_checkable, root, false)
            return populateView(view, contact, background)
        }
    }

}