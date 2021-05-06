package com.example.orangemeet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
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

    var contacts : List<Contact>? = null

    val checkedContacts = mutableListOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createMeetingFragment = inflater.inflate(R.layout.fragment_create_meeting, container, false)
        val calendar = Calendar.getInstance()

        pickedDate.value = calendar.time
        pickedDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {newDate ->
            dateView.setText(SimpleDateFormat("dd-MM-yyyy", Locale("PL")).format(newDate).toString())
            timeView.setText(SimpleDateFormat("kk:mm", Locale("PL")).format(newDate).toString())
        })

        searchView = createMeetingFragment.findViewById(R.id.searchView)
        contactsLayout = createMeetingFragment.findViewById(R.id.contactsLayout)
        dateBox = createMeetingFragment.findViewById(R.id.dateBox)
        timeBox = createMeetingFragment.findViewById(R.id.timeBox)
        dateView = createMeetingFragment.findViewById(R.id.meetingDate)
        timeView = createMeetingFragment.findViewById(R.id.meetingTime)
        createMeetingButton = createMeetingFragment.findViewById(R.id.createMeeting)
        meetingNameView = createMeetingFragment.findViewById(R.id.meetingName)

        createMeetingButton.setOnClickListener {
            if(meetingNameView.text!!.isEmpty()){
                Toast.makeText(requireContext(), "Nazwa spotkania nie może być pusta", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            BackendCommunication.CreateMeeting(requireContext(), pickedDate.value!!, meetingNameView.text.toString(), checkedContacts,
                    Response.Listener {
                        Toast.makeText(requireContext(), "Pomyślnie utworzono spotkanie", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    },
                    Response.ErrorListener {
                        Toast.makeText(requireContext(), "Nie udało się utworzyć spotkania", Toast.LENGTH_SHORT).show()
                    })
        }

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CreateViews()
                return true
            }
        })

        BackendCommunication.GetContactsList(requireContext(),
                Response.Listener {contacts ->
                    this.contacts = contacts
                    CreateViews()
                },
                Response.ErrorListener {
                    Toast.makeText(requireContext(), "Nie udało się pobrać listy spotkań", Toast.LENGTH_LONG).show()
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

    private fun CreateViews(){
        if(contacts == null)
            return

        val filteredContacts = contacts!!.filter { contact -> contact.username.toLowerCase().contains(searchView.query.toString().toLowerCase()) }

        contactsLayout.removeAllViews()

        filteredContacts!!.forEach {contact ->
            val contactView = Contact.createCheckView(layoutInflater, contactsLayout, contact, null)

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
        }
    }

}