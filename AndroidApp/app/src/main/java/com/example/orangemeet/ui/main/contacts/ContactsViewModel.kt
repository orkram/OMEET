package com.example.orangemeet.ui.main.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.User
import com.example.orangemeet.ui.utils.ErrorListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ContactsViewModel() : ViewModel() {

    private var errorListener : ErrorListener? = null

    private val _displayedContacts = MutableLiveData<List<User>>()
    val displayedContacts : LiveData<List<User>> = _displayedContacts

    private var contacts : List<User>? = null
    private var query : String = ""

    fun setOnErrorListener(onError: (Int) -> Unit){
        this.errorListener = object : ErrorListener{
            override fun onError(error: Int) {
                onError
            }
        }
    }

    fun updateQuery(query : String){
        if(contacts != null){
                this.query = query
                _displayedContacts.value = filteredContacts(query)
        }
    }

    private fun filteredContacts(query: String) : List<User>{
        if(contacts == null)
            return emptyList()

        val displayedContacts = contacts!!.filter  { contact ->
            if(query.isEmpty())
                true
            else
                contact.username.toLowerCase(Locale.ROOT).contains(query.toString().toLowerCase(Locale.ROOT))
        }

        return displayedContacts
    }

    fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    contacts = result.data!!
                    _displayedContacts.postValue(filteredContacts(query))
                }else{
                    errorListener?.onError(R.string.get_contacts_fail)
                }
            }
        }
    }

    fun deleteContact(contact : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.deleteContact(contact)
                if(result is Result.Success) {
                    getContacts()
                }else{
                    errorListener?.onError(R.string.contact_delete_fail)
                }
            }
        }
    }
}