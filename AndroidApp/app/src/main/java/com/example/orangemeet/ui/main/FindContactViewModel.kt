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
import timber.log.Timber

class FindContactViewModel() : ViewModel() {
    private var _getUsersResult = MutableLiveData<GetUsersResult>()
    val getUsersResult : LiveData<GetUsersResult> = _getUsersResult

    private var _getContactsResult = MutableLiveData<GetContactsResult>()
    val getContactsResult : LiveData<GetContactsResult> = _getContactsResult

    private var _addContactResult = MutableLiveData<AddContactResult>()
    val addContactResult : LiveData<AddContactResult> = _addContactResult

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    _getContactsResult.postValue(GetContactsResult(result.data, null))
                    Timber.i("getContacts success")
                }else{
                    _getContactsResult.postValue(GetContactsResult(null, R.string.get_contacts_fail))
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
                    _getUsersResult.postValue(GetUsersResult(result.data, null))
                }else{
                    _getUsersResult.postValue(GetUsersResult(null, R.string.get_users_fail))
                }
            }
        }
    }

    fun addContact(username : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.addContact(username)
                if(result is Result.Success){
                    _addContactResult.postValue(AddContactResult(true, null))
                }else{
                    _addContactResult.postValue(AddContactResult(false, R.string.add_contact_fail))
                }
            }
        }
    }
}