package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.User
import com.example.orangemeet.ui.utils.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FindContactViewModel() : ViewModel() {
    private var _getUsersResult = MutableLiveData<ResultInfo<List<User>>>()
    val getUsersResult : LiveData<ResultInfo<List<User>>> = _getUsersResult

    private var _getContactsResult = MutableLiveData<ResultInfo<List<User>>>()
    val getContactsResult : LiveData<ResultInfo<List<User>>> = _getContactsResult

    private var _addContactResult = MutableLiveData<ResultInfo<Nothing>>()
    val addContactResult : LiveData<ResultInfo<Nothing>> = _addContactResult

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    _getContactsResult.postValue(ResultInfo(true, result.data, null))
                    Timber.i("getContacts success")
                }else{
                    _getContactsResult.postValue(ResultInfo(false, null, R.string.get_contacts_fail))
                    Timber.e("getContacts failed: %s", result.toString())
                }
            }
        }
    }

    fun getUsers(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getUsers()
                if(result is Result.Success){
                    _getUsersResult.postValue(ResultInfo(true, result.data, null))
                }else{
                    _getUsersResult.postValue(ResultInfo(false, null, R.string.get_users_fail))
                }
            }
        }
    }

    fun addContact(username : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.addContact(username)
                if(result is Result.Success){
                    _addContactResult.postValue(ResultInfo(true, null, null))
                }else{
                    _addContactResult.postValue(ResultInfo(false, null, R.string.add_contact_fail))
                }
            }
        }
    }
}