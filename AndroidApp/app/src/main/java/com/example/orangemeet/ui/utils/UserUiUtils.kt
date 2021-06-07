package com.example.orangemeet.ui.utils

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.orangemeet.R
import com.example.orangemeet.data.model.User

class UserUiUtils{
    companion object{
        private fun populateView(view : View, contact : User, background: Drawable?) : View {
            val userNameTextView = view.findViewById<TextView>(R.id.contactUsername)
            val emailTextView = view.findViewById<TextView>(R.id.contactEmail)
            val box = view.findViewById<View>(R.id.box)
            userNameTextView?.text = contact.username
            emailTextView?.text = contact.email
            if(background != null)
                box.background = background
            return view
        }

        fun createSmallView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View {
            val view = inflater.inflate(R.layout.view_contact_details_small, root, false)
            view.findViewById<TextView>(R.id.contactUsername).text = contact.username
            return view
        }

        fun createView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View {
            val view = inflater.inflate(R.layout.item_contact, root, false)
            return populateView(view, contact, background)
        }

        fun createInviteView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View {
            val view = inflater.inflate(R.layout.item_contact_invite, root, false)
            return populateView(view, contact, background)
        }

        fun createCheckView(inflater : LayoutInflater, root : ViewGroup, contact: User, background: Drawable?) : View {
            val view = inflater.inflate(R.layout.item_contact_checkable, root, false)
            return populateView(view, contact, background)
        }
    }
}
