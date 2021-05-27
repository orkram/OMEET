package com.example.orangemeet.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.R
import com.example.orangemeet.User
import com.example.orangemeet.Util
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*


class CreateMeetingFragment : Fragment() {

    lateinit var progressBar : ProgressBar

    lateinit var dateBox : View
    lateinit var timeBox : View
    lateinit var dateView : MaterialTextView
    lateinit var timeView : MaterialTextView
    lateinit var contactsLayout : LinearLayout
    lateinit var searchView : SearchView
    lateinit var createMeetingButton : Button
    lateinit var meetingNameView : TextInputEditText

    val pickedDate = MutableLiveData<Date>()

    var contacts : List<User>? = null

    val checkedContacts = mutableListOf<User>()
    var includedContact : String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createMeetingFragment = inflater.inflate(R.layout.fragment_create_meeting, container, false)
        val calendar = Calendar.getInstance()

        includedContact = arguments?.getString("username")

        pickedDate.value = calendar.time
        pickedDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {newDate ->
            dateView.text = SimpleDateFormat("dd-MM-yyyy", Locale("PL")).format(newDate).toString()
            timeView.text = SimpleDateFormat("kk:mm", Locale("PL")).format(newDate).toString()
        })

        progressBar = createMeetingFragment.findViewById(R.id.progressBar)
        searchView = createMeetingFragment.findViewById(R.id.searchView)
        contactsLayout = createMeetingFragment.findViewById(R.id.contactsLayout)
        dateBox = createMeetingFragment.findViewById(R.id.dateBox)
        timeBox = createMeetingFragment.findViewById(R.id.timeBox)
        dateView = createMeetingFragment.findViewById(R.id.meetingDate)
        timeView = createMeetingFragment.findViewById(R.id.meetingTime)
        createMeetingButton = createMeetingFragment.findViewById(R.id.createMeeting)
        meetingNameView = createMeetingFragment.findViewById(R.id.meetingName)

        progressBar.visibility = View.VISIBLE

        createMeetingButton.setOnClickListener {
            if(meetingNameView.text!!.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.meeting_name_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            BackendCommunication.createMeeting(
                requireContext(),
                pickedDate.value!!,
                meetingNameView.text.toString(),
                checkedContacts,
                Response.Listener {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.create_meeting_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.create_meeting_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                })
        }

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                createViews()
                return true
            }
        })

        BackendCommunication.getContactsList(
            requireContext(),
            Response.Listener { contacts ->
                this.contacts = contacts
                checkedContacts.clear();
                contacts.forEach {contact ->
                    if(includedContact != null && contact.username == includedContact){
                        checkedContacts.add(contact)
                    }
                }
                progressBar.visibility = View.GONE;
                createViews()
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.get_meetings_fail),
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE;
            })

        dateBox.setOnClickListener {
            DatePickerDialog(requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        pickedDate.value = calendar.time
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) ).show()
        }

        timeBox.setOnClickListener {
            TimePickerDialog(context,
                    TimePickerDialog.OnTimeSetListener{ timePicker: TimePicker, hour: Int, minute: Int ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        val dat = calendar.time
                        pickedDate.value = calendar.time
                    },
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true ).show()
        }

        return createMeetingFragment
    }

    private fun createViews(){
        if(contacts == null)
            return

        val filteredContacts = contacts!!
                .filter { contact -> contact.username.toLowerCase().contains(searchView.query.toString().toLowerCase()) }
                .sortedBy { contact -> contact.username != includedContact}

        contactsLayout.removeAllViews()

        var evenView = false
        filteredContacts.forEach { contact ->

            val contactView =
                User.createCheckView(
                    layoutInflater,
                    contactsLayout,
                    contact,
                    Util.createTintedBackground(requireContext(), evenView)
                )

            val checkBox = contactView.findViewById<CheckBox>(R.id.checkBox)

            if(checkedContacts.contains(contact))
                checkBox.isChecked = true

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked)
                    checkedContacts.add(contact)
                else
                    checkedContacts.remove(contact)
            }

            contactsLayout.addView(contactView)
            evenView = !evenView
        }
    }

}