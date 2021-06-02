package com.example.orangemeet.services

import com.example.orangemeet.data.Result
import com.example.orangemeet.data.ResultListener
import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.User

interface DataSource {
    fun login(username : String, password : String) : Result<LoggedInUser>
    fun logout(username: String)
    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String) : Result<Void>
    fun getContacts(username: String) : Result<List<User>>
}