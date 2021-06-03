package com.example.orangemeet.services

import com.example.orangemeet.data.Result
import com.example.orangemeet.data.ResultListener
import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.User
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.*

interface DataSource {
    fun login(username : String, password : String) : Result<LoggedInUser>
    fun logout(username: String)
    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String) : Result<Void>
    fun getContacts() : Result<List<User>>
    fun deleteContact(contact : String) : Result<Void>
    fun getUsers() : Result<List<User>>
    fun addContact(contact : String) : Result<Void>
    fun getMeetings() : Result<List<Meeting>>
    fun createMeeting(date : Date, name : String, participants : List<User>) : Result<Void>
    fun getMeetingParticipants(meeting: Meeting) : Result<List<User>>
    fun getSettings() : Result<JSONObject>
    fun updateSettings(settingsJson : JSONObject) : Result<Void>
}