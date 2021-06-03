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

class ContactsViewModel() : ViewModel() {

    private var _getContactsResult = MutableLiveData<GetContactsResult>()
    val getContactsResult : LiveData<GetContactsResult> = _getContactsResult

    private var _deleteContactResult = MutableLiveData<DeleteContactResult>()
    val deleteContactResult : LiveData<DeleteContactResult> = _deleteContactResult

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    _getContactsResult.postValue(GetContactsResult(result.data!!, null))
                    Timber.i("getContacts success")
                }else{
                    _getContactsResult.postValue(GetContactsResult(null, R.string.get_contacts_fail))
                    Timber.e("getContacts failed: %s", result.toString())
                }
            }
        }
    }

    fun deleteContact(contact : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.deleteContact(contact)
                if(result is Result.Success){
                    _deleteContactResult.postValue(DeleteContactResult(true, null))
                }else{
                    _deleteContactResult.postValue(DeleteContactResult(false, R.string.contact_delete_fail))
                    Timber.e("deleteContact failed: %s", result.toString())
                }
            }
        }
    }
}