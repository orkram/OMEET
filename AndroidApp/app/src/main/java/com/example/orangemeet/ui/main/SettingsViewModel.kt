//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
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

class SettingsViewModel : ViewModel() {

    private val _updateSettingsResult = MutableLiveData<ResultInfo<List<Nothing>>>()
    val updateSettingsResult : LiveData<ResultInfo<List<Nothing>>> = _updateSettingsResult

    fun updateSettings(settingsJson : JSONObject){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.updateSettings(settingsJson)
                if(result is Result.Success){
                    _updateSettingsResult.postValue(ResultInfo(true, null, null))
                }else{
                    _updateSettingsResult.postValue(ResultInfo(false, null, R.string.get_settings_failed))
                }
            }
        }
    }
}