package com.example.orangemeet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class ContactsFragment : Fragment() {

    final var testContacts = Array<Contact>(20){i -> Contact() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contactsFragment = inflater.inflate(R.layout.fragment_contacts, container, false)
        val contactsListView = contactsFragment.findViewById<LinearLayout>(R.id.contactsList)

        var primaryColor = TypedValue()
        var secondaryColor = TypedValue()
        context?.theme?.resolveAttribute(R.attr.background, primaryColor, true)
        context?.theme?.resolveAttribute(R.attr.secondaryBackground, secondaryColor, true)

        var useSecondaryColor : Boolean = false
        testContacts.forEach {
            val color = ColorDrawable(resources.getColor(
                    if(useSecondaryColor == true)
                        primaryColor.resourceId
                    else
                        secondaryColor.resourceId))

            val view = Contact.createView(inflater, contactsListView, Contact(), color)

            contactsListView.addView(view)
            useSecondaryColor = !useSecondaryColor
        }

        return contactsFragment
    }
}