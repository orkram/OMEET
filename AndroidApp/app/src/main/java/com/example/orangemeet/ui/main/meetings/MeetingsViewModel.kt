//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.main.meetings

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.User
import com.example.orangemeet.data.model.UserAvatarPair
import com.example.orangemeet.ui.utils.ErrorListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MeetingsViewModel() : ViewModel() {

    private var errorListener : ErrorListener? = null

    private val _meetingsParticipants = MutableLiveData<List<UserAvatarPair>>()
    val meetingsParticipants : LiveData<List<UserAvatarPair>> = _meetingsParticipants

    private val _displayedMeetings = MutableLiveData<List<Meeting>>()
    val displayedMeetings : LiveData<List<Meeting>> = _displayedMeetings

    private val _ownerAvatar = MutableLiveData<Bitmap?>()
    val ownerAvatar : LiveData<Bitmap?> = _ownerAvatar

    private var query : String = ""
    private var meetings : List<Meeting>? = null


    fun updateQuery(query : String){
        this.query = query
        if(meetings != null)
            _displayedMeetings.value = filteredMeetings()
    }

    fun setOnErrorListener(onError: (Int) -> Unit){
        this.errorListener = object : ErrorListener{
            override fun onError(error: Int) {
                onError(error)
            }
        }
    }

    private fun filteredMeetings() : List<Meeting> {
        if(meetings == null)
            return emptyList()

        val filteredMeetings = meetings!!.filter { meeting ->
            if(query.isEmpty())
                true
            else
                meeting.name.toLowerCase(Locale.ROOT).contains(query.toString().toLowerCase(Locale.ROOT))
        }
        return filteredMeetings
    }

    fun getMeetings(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetings()
                if(result is Result.Success){
                    meetings = result.data!!
                    _displayedMeetings.postValue(filteredMeetings())
                }else{
                    withContext(Dispatchers.Main){
                        errorListener?.onError(R.string.get_meetings_fail)
                    }
                }
            }
        }
    }

    fun getMeetingParticipants(meeting : Meeting){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getMeetingParticipants(meeting)
                if(result is Result.Success){
                    val participants = result.data!!
                    val participantsWithAvatars = mutableListOf<UserAvatarPair>()
                    participants.forEach { participant ->
                        if (participant != meeting.owner) {
                            val getAvatarResult = DataRepository.getAvatar(participant)
                            if (getAvatarResult is Result.Success)
                                participantsWithAvatars.add(UserAvatarPair(participant, getAvatarResult.data))
                            else
                                participantsWithAvatars.add(UserAvatarPair(participant, null))
                        }
                    }
                    _meetingsParticipants.postValue(participantsWithAvatars)
                }else{
                    withContext(Dispatchers.Main) {
                        errorListener?.onError(R.string.get_meetings_participants_failed)
                    }
                }
            }
        }
    }

    fun getAvatar(user : User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val getAvatarResult = DataRepository.getAvatar(user)
                if(getAvatarResult is Result.Success)
                    _ownerAvatar.postValue(getAvatarResult.data!!)
                else
                    _ownerAvatar.postValue(null)
            }
        }
    }
}