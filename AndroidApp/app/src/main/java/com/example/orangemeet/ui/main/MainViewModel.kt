package com.example.orangemeet.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.User
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class MainViewModel() : ViewModel() {

    private val _getSettingsResult = MutableLiveData<ResultInfo<JSONObject>>()
    val getSettingsResult : LiveData<ResultInfo<JSONObject>> = _getSettingsResult

    private val _getAvatarResult = MutableLiveData<ResultInfo<Bitmap>>()
    val getAvatarResult : LiveData<ResultInfo<Bitmap>> = _getAvatarResult

    fun logout(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                DataRepository.logout()
            }
        }
    }

    fun getAvatar() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val getUserResult = DataRepository.getUser(DataRepository.loggedInUser!!.username)
                if (getUserResult is Result.Success) {
                    val getAvatarResult =
                        DataRepository.getAvatar(getUserResult.data!!)
                    if(getAvatarResult is Result.Success){
                        _getAvatarResult.postValue(ResultInfo(true, getAvatarResult.data, null))
                    }else{
                        Timber.e(getAvatarResult.toString())
                        _getAvatarResult.postValue(ResultInfo(false, null, R.string.get_settings_failed))
                    }
                } else {
                    Timber.e(getUserResult.toString())
                    _getAvatarResult.postValue(ResultInfo(false, null, R.string.get_settings_failed))
                }
            }
        }
    }

    fun getSettings(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getSettings()
                if(result is Result.Success){
                    _getSettingsResult.postValue(ResultInfo(true, result.data, null))
                }else{
                    _getSettingsResult.postValue(ResultInfo(false, null, R.string.get_settings_failed))
                }
            }
        }
    }
}