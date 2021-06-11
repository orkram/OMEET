//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.main.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.R
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.UserAvatarPair
import com.example.orangemeet.ui.utils.ErrorListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ContactsViewModel() : ViewModel() {

    private var errorListener : ErrorListener? = null

    private val _displayedContacts = MutableLiveData<List<UserAvatarPair>>()
    val displayedContacts : LiveData<List<UserAvatarPair>> = _displayedContacts

    private var contactsWithAvatars : List<UserAvatarPair>? = null
    private var query : String = ""

    fun setOnErrorListener(onError: (Int) -> Unit){
        this.errorListener = object : ErrorListener{
            override fun onError(error: Int) {
                onError(error)
            }
        }
    }

    fun updateQuery(query : String){
        if(contactsWithAvatars != null){
                this.query = query
                _displayedContacts.value = filteredContacts(query)
        }
    }

    private fun filteredContacts(query: String) : List<UserAvatarPair>{
        if(contactsWithAvatars == null)
            return emptyList()

        val displayedContacts = contactsWithAvatars!!.filter  { contactAvatarPair ->
            if(query.isEmpty())
                true
            else
                contactAvatarPair.user.username.toLowerCase(Locale.ROOT)
                    .contains(query.toString().toLowerCase(Locale.ROOT))
        }

        return displayedContacts
    }

    fun refreshContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    val contactsWithAvatars = mutableListOf<UserAvatarPair>()
                    result.data!!.forEach { contact ->
                        val getAvatarResult = DataRepository.getAvatar(contact)
                        if (getAvatarResult is Result.Success)
                            contactsWithAvatars.add(UserAvatarPair(contact, getAvatarResult.data))
                        else
                            contactsWithAvatars.add(UserAvatarPair(contact, null))
                    }
                    this@ContactsViewModel.contactsWithAvatars = contactsWithAvatars
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
                    refreshContacts()
                }else{
                    errorListener?.onError(R.string.contact_delete_fail)
                }
            }
        }
    }
}