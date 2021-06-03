package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.User
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MeetingsViewModel() : ViewModel() {

    private var _getMeetingsResult = MutableLiveData<ResultInfo<List<Meeting>>>()
    val getMeetingsResult : LiveData<ResultInfo<List<Meeting>>> = _getMeetingsResult

    private var _getMeetingParticipantsResult = MutableLiveData<ResultInfo<List<User>>>()
    val getMeetingParticipantsResult : LiveData<ResultInfo<List<User>>> = _getMeetingParticipantsResult

    fun getMeetings(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetings()
                if(result is Result.Success){
                    _getMeetingsResult.postValue(ResultInfo(true, result.data, null))
                }else{
                    _getMeetingsResult.postValue(ResultInfo(false, null, R.string.get_meetings_fail))
                }
            }
        }
    }

    fun getMeetingParticipants(meeting : Meeting){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetingParticipants(meeting)
                if(result is Result.Success){
                    _getMeetingParticipantsResult.postValue(ResultInfo(true, result.data!!, null))
                }else{
                    _getMeetingParticipantsResult.postValue(ResultInfo(false, null, R.string.get_meetings_participants_failed))
                }
            }
        }
    }
}