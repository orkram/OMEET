package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.Meeting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MeetingsViewModel() : ViewModel() {

    private var _getMeetingsResult = MutableLiveData<GetMeetingsResult>()
    val getMeetingsResult : LiveData<GetMeetingsResult> = _getMeetingsResult

    private var _getMeetingParticipantsResult = MutableLiveData<GetMeetingParticipantsResult>()
    val getMeetingParticipantsResult : LiveData<GetMeetingParticipantsResult> = _getMeetingParticipantsResult

    fun getMeetings(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetings()
                if(result is Result.Success){
                    _getMeetingsResult.postValue(GetMeetingsResult(result.data, null))
                }else{
                    _getMeetingsResult.postValue(GetMeetingsResult(null, R.string.get_meetings_fail))
                }
            }
        }
    }

    fun getMeetingParticipants(meeting : Meeting){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetingParticipants(meeting)
                if(result is Result.Success){
                    _getMeetingParticipantsResult.postValue(GetMeetingParticipantsResult(result.data!!, null))
                }else{
                    _getMeetingParticipantsResult.postValue(GetMeetingParticipantsResult(null, R.string.get_meetings_participants_failed))
                }
            }
        }
    }
}