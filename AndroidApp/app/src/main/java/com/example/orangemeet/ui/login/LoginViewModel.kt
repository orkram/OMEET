package com.example.orangemeet.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.data.DataRepository

import com.example.orangemeet.R
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(private val sharedPrefs : SharedPreferences) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<ResultInfo<LoggedInUser>>()
    val loginResult: LiveData<ResultInfo<LoggedInUser>> = _loginResult

    init {
        val dayNightMode = sharedPrefs.getBoolean("day_night", false)
        AppCompatDelegate.setDefaultNightMode(if (dayNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.login(username, password)
                if(result is Result.Success){
                    _loginResult.postValue(ResultInfo(true, data = result.data))
                }else{
                    Timber.e(result.toString())
                    _loginResult.postValue(ResultInfo(false, error = R.string.login_failed))
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.username_must_not_be_empty)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.password_must_not_be_empty)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }
}