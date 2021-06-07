package com.example.orangemeet.ui.register

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

class RegisterViewModel() : ViewModel() {

    private val _registerResult = MutableLiveData<ResultInfo<Nothing>>()
    val registerResult: LiveData<ResultInfo<Nothing>> = _registerResult

    private fun inputsValid(email : String, firstName : String, lastName : String,
                            imgUrl : String, username : String, password : String, passwordRepeat : String) : Boolean{

        if(email.isEmpty() or firstName.isEmpty() or lastName.isEmpty() or imgUrl.isEmpty()
                or username.isEmpty() or password.isEmpty() or passwordRepeat.isEmpty()){
            _registerResult.value = ResultInfo(false, error = R.string.fields_must_be_not_empty)
            return false
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerResult.value = ResultInfo(false, error = R.string.email_not_correct)
            return false
        }
        if(password != passwordRepeat) {
            _registerResult.value = ResultInfo(false, error = R.string.passwords_dont_match)
            return false
        }
        if(password.length < 8) {
            _registerResult.value = ResultInfo(false, error = R.string.password_too_short)
            return false
        }

        return true //no errors
    }

    private fun launchRegisterCoroutine(email : String, firstName : String, lastName : String,
                                        imgUrl : String, username : String, password : String, passwordRepeat : String){
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

    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String, passwordRepeat : String){

        if(!inputsValid(email, firstName, lastName, imgUrl, username, password, passwordRepeat))
            return

        launchRegisterCoroutine(email, firstName, lastName, imgUrl, username, password, passwordRepeat)
    }
}