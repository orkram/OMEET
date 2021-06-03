package com.example.orangemeet.ui.main.meetings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {
    private var _getMeetingsResult = MutableLiveData<ResultInfo<List<Meeting>>>()
    val getMeetingsResult : LiveData<ResultInfo<List<Meeting>>> = _getMeetingsResult

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
}