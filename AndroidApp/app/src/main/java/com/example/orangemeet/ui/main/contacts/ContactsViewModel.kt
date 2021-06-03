package com.example.orangemeet.ui.main.contacts

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

class ContactsViewModel() : ViewModel() {

    private var _getContactsResult = MutableLiveData<ResultInfo<List<User>>>()
    val getContactsResult : LiveData<ResultInfo<List<User>>> = _getContactsResult

    private var _deleteContactResult = MutableLiveData<ResultInfo<Nothing>>()
    val deleteContactResult : LiveData<ResultInfo<Nothing>> = _deleteContactResult

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    _getContactsResult.postValue(ResultInfo(true, result.data!!, null))
                    Timber.i("getContacts success")
                }else{
                    _getContactsResult.postValue(ResultInfo(false, null, R.string.get_contacts_fail))
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
                    _deleteContactResult.postValue(ResultInfo(true, null, null))
                }else{
                    _deleteContactResult.postValue(ResultInfo(false, null, R.string.contact_delete_fail))
                    Timber.e("deleteContact failed: %s", result.toString())
                }
            }
        }
    }
}