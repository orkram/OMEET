package com.example.orangemeet.ui.main.meetings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.orangemeet.R
import com.example.orangemeet.data.model.User
import com.example.orangemeet.utils.Util
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*


class CreateMeetingFragment : Fragment() {

    lateinit var createMeetingViewModel: CreateMeetingViewModel

    lateinit var getContactsProgressBar : ProgressBar

    lateinit var dateBox : View
    lateinit var timeBox : View
    lateinit var dateView : MaterialTextView
    lateinit var timeView : MaterialTextView
    lateinit var contactsLayout : LinearLayout
    lateinit var searchView : SearchView
    lateinit var createMeetingButton : Button
    lateinit var meetingNameView : TextInputEditText



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createMeetingFragment = inflater.inflate(R.layout.fragment_create_meeting, container, false)

        createMeetingViewModel = ViewModelProvider(this).get(CreateMeetingViewModel::class.java)

        val includedUser = arguments?.getParcelable<User>("user")
        if(includedUser != null)
            createMeetingViewModel.setIncludedContact(includedUser)

        createMeetingViewModel.pickedDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {newDate ->
            dateView.text = SimpleDateFormat("dd-MM-yyyy", Locale("PL")).format(newDate).toString()
            timeView.text = SimpleDateFormat("kk:mm", Locale("PL")).format(newDate).toString()
        })

        getContactsProgressBar = createMeetingFragment.findViewById(R.id.progressBar)
        searchView = createMeetingFragment.findViewById(R.id.searchView)
        contactsLayout = createMeetingFragment.findViewById(R.id.contactsLayout)
        dateBox = createMeetingFragment.findViewById(R.id.dateBox)
        timeBox = createMeetingFragment.findViewById(R.id.timeBox)
        dateView = createMeetingFragment.findViewById(R.id.meetingDate)
        timeView = createMeetingFragment.findViewById(R.id.meetingTime)
        createMeetingButton = createMeetingFragment.findViewById(R.id.createMeeting)
        meetingNameView = createMeetingFragment.findViewById(R.id.meetingName)

        createMeetingViewModel.createMeetingResult.observe(viewLifecycleOwner, Observer {result ->
            if(result.success){
                showMessage(R.string.create_meeting_success)
                findNavController().navigateUp()
            }else{
                showError(R.string.create_meeting_fail)
            }
        })

        meetingNameView.setText(createMeetingViewModel.meetingName)
        meetingNameView.addTextChangedListener {text ->
            if (text != null) {
                createMeetingViewModel.meetingName = text.toString()
            }
        }

        createMeetingButton.setOnClickListener {
            if(meetingNameView.text!!.isEmpty()){
                showMessage(R.string.meeting_name_empty)
                return@setOnClickListener
            }
            createMeetingViewModel.createMeeting()
        }

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { return true }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    createMeetingViewModel.updateQuery(newText)
                return true
            }
        })

        createMeetingViewModel.displayedContacts.observe(viewLifecycleOwner,
                Observer {contacts ->
                    getContactsProgressBar.visibility = View.GONE;
                    displayContacts(contacts)
                })

        dateBox.setOnClickListener {
            setDateDialog()
        }

        timeBox.setOnClickListener {
            setTimeDialog()
        }

        createMeetingViewModel.setOnErrorListener { error ->
            showError(error)
        }

        getContactsProgressBar.visibility = View.VISIBLE
        createMeetingViewModel.getContacts()

        return createMeetingFragment
    }

    private fun setDateDialog() {
        val meetingCalendar = createMeetingViewModel.getMeetingCalendar()
        DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    createMeetingViewModel.setMeetingDate(year, month, dayOfMonth)
                },
                meetingCalendar.get(Calendar.YEAR),
                meetingCalendar.get(Calendar.MONTH),
                meetingCalendar.get(Calendar.DAY_OF_MONTH) ).show()
    }

    private fun setTimeDialog() {
        val meetingCalendar = createMeetingViewModel.getMeetingCalendar()
        TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener{ timePicker: TimePicker, hour: Int, minute: Int ->
                    createMeetingViewModel.setMeetingTime(hour, minute)
                },
                meetingCalendar.get(Calendar.HOUR_OF_DAY),
                meetingCalendar.get(Calendar.MINUTE),
                true ).show()
    }

    private fun showError(@StringRes error : Int) {
        Toast.makeText(
                requireContext(),
                getString(error),
                Toast.LENGTH_LONG
        ).show()
    }

    private fun showMessage(@StringRes message : Int) {
        Toast.makeText(
                requireContext(),
                getString(message),
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun createContactItem(contact : User, evenView : Boolean) : View {
        val contactItem =
                User.createCheckView(
                        layoutInflater,
                        contactsLayout,
                        contact,
                        Util.createTintedBackground(requireContext(), evenView)
                )

        val checkBox = contactItem.findViewById<CheckBox>(R.id.checkBox)

        checkBox.isChecked = createMeetingViewModel.isContactChecked(contact)

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            createMeetingViewModel.setContactChecked(contact, isChecked)
        }

        return contactItem
    }

    private fun displayContacts(contacts : List<User>){
        contactsLayout.removeAllViews()

        var evenView = false
        contacts.forEach { contact ->
            contactsLayout.addView(createContactItem(contact, evenView))
            evenView = !evenView
        }
    }

}