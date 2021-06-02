package com.example.orangemeet.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.data.BackendRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ContactsViewModel() : ViewModel() {

    private var _getContactsResult = MutableLiveData<List<User>?>()
    val getContactsResult : LiveData<List<User>?> = _getContactsResult

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = BackendRepository.getContacts()
                if(result is Result.Success){
                    _getContactsResult.postValue(result.data!!)
                    Timber.i("getContacts success")
                }else{
                    _getContactsResult.postValue(null)
                    Timber.e("getContacts failed: %s", result.toString())
                }
            }
        }
    }
}