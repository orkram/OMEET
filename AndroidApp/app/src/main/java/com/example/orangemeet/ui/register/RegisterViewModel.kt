package com.example.orangemeet.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel() : ViewModel() {

    private val _registerResult = MutableLiveData<ResultInfo<Nothing>>()
    val registerResult: LiveData<ResultInfo<Nothing>> = _registerResult

    //registerresult

    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String){

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.register(email, firstName, lastName, imgUrl, username, password)
                if(result is Result.Success)
                    _registerResult.postValue(ResultInfo(true))
                else
                    _registerResult.postValue(ResultInfo(false, error =  R.string.registed_failed))
            }
        }
    }
}