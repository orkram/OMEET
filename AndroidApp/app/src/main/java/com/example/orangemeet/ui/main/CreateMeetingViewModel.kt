package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateMeetingViewModel : ViewModel() {

    private var _createMeetingResult = MutableLiveData<CreateMeetingResult>()
    val createMeetingResult : LiveData<CreateMeetingResult> = _createMeetingResult

    private var _getContactsResult = MutableLiveData<GetContactsResult>()
    val getContactsResult : LiveData<GetContactsResult> = _getContactsResult


    fun createMeeting(date: Date, name: String, participants: List<User>){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.createMeeting(date, name, participants)
                if(result is Result.Success){
                    _createMeetingResult.postValue(CreateMeetingResult(true, null))
                }else{
                    _createMeetingResult.postValue(CreateMeetingResult(false, R.string.create_meeting_fail))
                }
            }
        }
    }


    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    _getContactsResult.postValue(GetContactsResult(result.data!!, null))
                }else{
                    _getContactsResult.postValue(GetContactsResult(null, R.string.get_contacts_fail))
                }
            }
        }
    }

}