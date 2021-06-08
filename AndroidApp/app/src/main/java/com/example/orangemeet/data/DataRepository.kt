package com.example.orangemeet.data

import android.graphics.Bitmap
import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.User
import com.example.orangemeet.services.BackendService
import com.example.orangemeet.services.DataSource
import org.json.JSONObject
import java.util.*

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

object DataRepository {

    val dataSource : DataSource = BackendService()

    var loggedInUser: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = loggedInUser != null

    fun logout() {
        if(loggedInUser == null)
            return

        dataSource.logout(loggedInUser!!.username)
        loggedInUser = null
    }

    fun login(username: String, password: String) : Result<LoggedInUser> {
        val result = dataSource.login(username, password)
        if(result is Result.Success)
            loggedInUser = result.data
        return result
    }

    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String) : Result<Nothing> {
        return dataSource.register(email, firstName, lastName, imgUrl, username, password)
    }

    fun getContacts() : Result<List<User>> {
        return dataSource.getContacts()
    }

    fun deleteContact(contact: String): Result<Nothing> {
        return dataSource.deleteContact(contact)
    }

    fun getUsers() : Result<List<User>> {
        return dataSource.getUsers()
    }

    fun addContact(contact: String): Result<Nothing> {
        return dataSource.addContact(contact)
    }

    fun getMeetings() : Result<List<Meeting>> {
        return dataSource.getMeetings()
    }

    fun createMeeting(date: Date, name: String, participants: List<User>): Result<Nothing> {
        return dataSource.createMeeting(date, name, participants)
    }

    fun getMeetingParticipants(meeting: Meeting) : Result<List<User>> {
        return dataSource.getMeetingParticipants(meeting)
    }

    fun getSettings() : Result<JSONObject> {
        return dataSource.getSettings()
    }

    fun updateSettings(settingsJson : JSONObject) : Result<Nothing> {
        return dataSource.updateSettings(settingsJson)
    }

    fun getAvatar(user : User) : Result<Bitmap> {
        return dataSource.getAvatar(user.imgUrl)
    }

    fun getUser(username : String) : Result<User> {
        return dataSource.getUser(username)
    }
}