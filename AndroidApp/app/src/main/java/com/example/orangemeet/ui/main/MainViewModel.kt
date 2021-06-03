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

class MainViewModel() : ViewModel() {

    private val _getSettingsResult = MutableLiveData<GetSettingsResult>()
    val getSettingsResult : LiveData<GetSettingsResult> = _getSettingsResult

    fun logout(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                DataRepository.logout()
            }
        }
    }

    fun getSettings(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getSettings()
                if(result is Result.Success){
                    _getSettingsResult.postValue(GetSettingsResult(result.data, null))
                }else{
                    _getSettingsResult.postValue(GetSettingsResult(null, R.string.get_settings_failed))
                }
            }
        }
    }
}