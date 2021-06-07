package com.example.orangemeet.data.model

import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.orangemeet.R
import com.example.orangemeet.utils.Util
import org.json.JSONObject


data class User(val username : String, val email : String) : Parcelable{

    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object{

        val CREATOR = object : Parcelable.Creator<User>{
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }

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
            val view = inflater.inflate(R.layout.view_contact_details_small, root, false)
            view.findViewById<TextView>(R.id.contactUsername).text = contact.username
            return view
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.item_contact, root, false)
            return populateView(view, contact, background)
        }

        fun createInviteView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.item_contact_invite, root, false)
            return populateView(view, contact, background)
        }

        fun createCheckView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View{
            val view = inflater.inflate(R.layout.item_contact_checkable, root, false)
            return populateView(view, contact, background)
        }
    }




}