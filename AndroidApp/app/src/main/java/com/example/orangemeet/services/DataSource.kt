package com.example.orangemeet.services

import android.graphics.Bitmap
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.User
import org.json.JSONObject
import java.util.*

interface DataSource {
    fun login(username : String, password : String) : Result<LoggedInUser>
    fun logout(username: String)
    fun register(email: String, firstName: String, lastName: String,
                 imgUrl: String, username: String, password: String): Result<Nothing>

    fun getContacts() : Result<List<User>>
    fun deleteContact(contact: String): Result<Nothing>
    fun getUsers() : Result<List<User>>
    fun addContact(contact: String): Result<Nothing>
    fun getMeetings() : Result<List<Meeting>>
    fun createMeeting(date: Date, name: String, participants: List<User>): Result<Nothing>
    fun getMeetingParticipants(meeting: Meeting) : Result<List<User>>
    fun getSettings() : Result<JSONObject>
    fun updateSettings(settingsJson: JSONObject): Result<Nothing>
    fun getAvatar(imgUrl: String) : Result<Bitmap>
    fun getUser(username: String) : Result<User>
}