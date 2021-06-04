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
import com.example.orangemeet.ui.utils.ResultInfo
import com.example.orangemeet.ui.utils.ResultInfoListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindContactViewModel() : ViewModel() {

    private var errorListener : ErrorListener? = null

    private val _displayedUsers = MutableLiveData<List<User>>()
    val displayedUsers : LiveData<List<User>> = _displayedUsers

    private var contacts : List<User>? = null
    private var users : List<User>? = null

    private var query : String = ""

    fun setOnErrorListener(onError : (Int) -> Unit){
        this.errorListener = object : ErrorListener{
            override fun onError(error: Int) {
                onError(error)
            }
        }
    }

    fun updateQuery(query : String){
        this.query = query
        _displayedUsers.value = filteredUsers()
    }

    private fun filteredUsers() : List<User>{
        if(contacts == null || users == null)
            return emptyList()

        return users!!.filter { user ->
            contacts!!.none { contact -> contact.username == user.username } &&
                    user.username != DataRepository.loggedInUser!!.username &&
                    user.username.contains(query, ignoreCase = true)
        }
    }

    fun refreshUsers(){
        getContacts()
        getUsers()
    }

    private fun getContacts(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getContacts()
                if(result is Result.Success){
                    contacts = result.data
                    if(users != null){
                        _displayedUsers.postValue(filteredUsers())
                    }else{ }
                }else{
                    errorListener?.onError(R.string.get_contacts_fail)
                }
            }
        }
    }

    private fun getUsers(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.getUsers()
                if(result is Result.Success){
                    users = result.data
                    if(contacts != null)
                        _displayedUsers.postValue(filteredUsers())
                    else{ }
                }else{
                    errorListener?.onError(R.string.get_users_fail)
                }
            }
        }
    }

    fun addContact(username : String, onResultInfo : (ResultInfo<Nothing>) -> Unit){
        val onResultInfoListener = object : ResultInfoListener<Nothing>{
            override fun onResult(resultInfo: ResultInfo<Nothing>) {
                onResultInfo(resultInfo)
            }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = DataRepository.addContact(username)
                viewModelScope.launch {
                    withContext(Dispatchers.Main){
                        if(result is Result.Success){
                            onResultInfoListener.onResult(ResultInfo(true))
                        }else{
                            onResultInfoListener.onResult(ResultInfo(false, error = R.string.add_contact_fail))
                        }
                    }
                }
            }
        }
    }
}