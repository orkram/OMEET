package com.example.orangemeet.ui.start

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

class StartViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<ResultInfo<Nothing>>()
    val loginResult : LiveData<ResultInfo<Nothing>> = _loginResult

    fun userLoggedIn() : Boolean {
        return DataRepository.loggedInUser != null
    }

    fun login(username : String, password : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.login(username, password)
                if(result is Result.Success)
                    _loginResult.postValue(ResultInfo(success = true))
                else
                    _loginResult.postValue(ResultInfo(success = false, error = R.string.login_failed))
            }
        }
    }
}