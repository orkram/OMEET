//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.main.meetings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.User
import com.example.orangemeet.data.model.UserAvatarPair
import com.example.orangemeet.ui.utils.ErrorListener
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateMeetingViewModel : ViewModel() {

    private var errorListener : ErrorListener? = null

    private val _createMeetingResult = MutableLiveData<ResultInfo<Nothing>>()
    val createMeetingResult : LiveData<ResultInfo<Nothing>> = _createMeetingResult

    private val _displayedContacts = MutableLiveData<List<UserAvatarPair>>()
    val displayedContacts : LiveData<List<UserAvatarPair>> = _displayedContacts

    private val _pickedDate = MutableLiveData<Date>()
    val pickedDate : LiveData<Date> = _pickedDate

    val checkedContacts = mutableListOf<User>()

    private var query : String = ""
    private var contactsWithAvatars : List<UserAvatarPair>? = null
    private var includedContact : User? = null

    private val meetingCalendar = Calendar.getInstance()

    var meetingName : String = ""

    init {
        _pickedDate.value = meetingCalendar.time
    }

    fun setOnErrorListener(onError: (Int) -> Unit){
        this.errorListener = object : ErrorListener{
            override fun onError(error: Int) {
                onError(error)
            }
        }
    }

    fun getMeetingCalendar() : Calendar {
        val meetingCalendar = Calendar.getInstance()
        meetingCalendar.time = pickedDate.value!!
        return meetingCalendar
    }

    fun setMeetingDate(year : Int, month : Int, dayOfMonth : Int) {
        meetingCalendar.set(year, month, dayOfMonth)
        _pickedDate.value = meetingCalendar.time
    }

    fun setMeetingTime(hour : Int, minute : Int) {
        meetingCalendar.set(Calendar.HOUR_OF_DAY, hour)
        meetingCalendar.set(Calendar.MINUTE, minute)
        _pickedDate.value = meetingCalendar.time
    }

    fun setIncludedContact(includedContact : User) {
        this.includedContact = includedContact
        setContactChecked(includedContact, true)
    }

    fun updateQuery(query : String) {
        this.query = query
        _displayedContacts.value = filteredContacts()
    }

    private fun filteredContacts() : List<UserAvatarPair> {
        if(contactsWithAvatars == null)
            return emptyList()

        val filteredContacts = contactsWithAvatars!!
                .filter { contact -> contact.user.username.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) }
        if(includedContact != null)
                return filteredContacts.sortedBy { contact -> contact.user != includedContact}
        else
            return filteredContacts
    }

    fun setContactChecked(contact : User, checked : Boolean) {
        if(checked && !checkedContacts.contains(contact)){
            checkedContacts.add(contact)
        } else {
            checkedContacts.remove(contact)
        }
    }

    fun isContactChecked(contact : User) : Boolean{
        return checkedContacts.contains(contact)
    }

    fun createMeeting(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.createMeeting(pickedDate.value!!, meetingName, checkedContacts)
                if(result is Result.Success){
                    _createMeetingResult.postValue(ResultInfo(true, null, null))
                }else{
                    _createMeetingResult.postValue(ResultInfo(false, null, R.string.create_meeting_fail))
                }
            }
        }
    }

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    val contactsWithAvatars = mutableListOf<UserAvatarPair>()
                    result.data!!.forEach { contact ->
                        val getAvatarResult = DataRepository.getAvatar(contact)
                        if (getAvatarResult is Result.Success) {
                            contactsWithAvatars.add(UserAvatarPair(contact, getAvatarResult.data))
                        } else {
                            contactsWithAvatars.add(UserAvatarPair(contact, null))
                        }
                    }
                    this@CreateMeetingViewModel.contactsWithAvatars = contactsWithAvatars
                    _displayedContacts.postValue(filteredContacts())
                }else{
                    errorListener?.onError( R.string.get_contacts_fail)
                }
            }
        }
    }

}