package com.example.orangemeet.ui.main.meetings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.User
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateMeetingViewModel : ViewModel() {

    private var _createMeetingResult = MutableLiveData<ResultInfo<Nothing>>()
    val createMeetingResult : LiveData<ResultInfo<Nothing>> = _createMeetingResult

    private var _getContactsResult = MutableLiveData<ResultInfo<List<User>>>()
    val getContactsResult : LiveData<ResultInfo<List<User>>> = _getContactsResult


    fun createMeeting(date: Date, name: String, participants: List<User>){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.createMeeting(date, name, participants)
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
                    _getContactsResult.postValue(ResultInfo(true, result.data!!, null))
                }else{
                    _getContactsResult.postValue(ResultInfo(false, null, R.string.get_contacts_fail))
                }
            }
        }
    }

}