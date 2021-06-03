package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SettingsViewModel : ViewModel() {

    private val _updateSettingsResult = MutableLiveData<UpdateSettingsResult>()
    val updateSettingsResult : LiveData<UpdateSettingsResult> = _updateSettingsResult

    fun updateSettings(settingsJson : JSONObject){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.updateSettings(settingsJson)
                if(result is Result.Success){
                    _updateSettingsResult.postValue(UpdateSettingsResult(true, null))
                }else{
                    _updateSettingsResult.postValue(UpdateSettingsResult(false, R.string.get_settings_failed))
                }
            }
        }
    }
}