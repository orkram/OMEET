package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainViewModel() : ViewModel() {

    private val _getSettingsResult = MutableLiveData<ResultInfo<JSONObject>>()
    val getSettingsResult : LiveData<ResultInfo<JSONObject>> = _getSettingsResult

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
                    _getSettingsResult.postValue(ResultInfo(true, result.data, null))
                }else{
                    _getSettingsResult.postValue(ResultInfo(false, null, R.string.get_settings_failed))
                }
            }
        }
    }
}