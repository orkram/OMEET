package com.example.orangemeet.ui.main.meetings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CalendarViewModel : ViewModel() {
    private var _getMeetingsResult = MutableLiveData<ResultInfo<List<Meeting>>>()
    val getMeetingsResult : LiveData<ResultInfo<List<Meeting>>> = _getMeetingsResult

    private var _selectedDayMeetings = MutableLiveData<List<Meeting>>()
    val selectedDayMeetings : LiveData<List<Meeting>> = _selectedDayMeetings

    private var meetings : List<Meeting>? = null
    private var selectedDay = Calendar.getInstance().time

    fun selectDay(day : Date) {
        selectedDay = day
        _selectedDayMeetings.value = filteredMeetings()
    }

    private fun filteredMeetings() : List<Meeting> {
        if(meetings == null)
            return emptyList()

        val filteredMeetings = meetings!!.filter { meeting ->
            val meetingCalendar = Calendar.getInstance().apply { time = meeting.date }
            val selectedDayCalendar = Calendar.getInstance().apply{ time = selectedDay }
            meetingCalendar.get(Calendar.DAY_OF_YEAR) == selectedDayCalendar.get(Calendar.DAY_OF_YEAR) &&
                    meetingCalendar.get(Calendar.YEAR) == selectedDayCalendar.get(Calendar.YEAR)
        }

        return filteredMeetings
    }

    fun getMeetings(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetings()
                if(result is Result.Success){
                    meetings = result.data
                    _getMeetingsResult.postValue(ResultInfo(true, result.data, null))
                }else{
                    _getMeetingsResult.postValue(ResultInfo(false, null, R.string.get_meetings_fail))
                }
            }
        }
    }
}