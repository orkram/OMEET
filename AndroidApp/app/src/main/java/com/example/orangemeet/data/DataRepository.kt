package com.example.orangemeet.data

import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.Meeting
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

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    fun logout() {
        if(user == null)
            return

        dataSource.logout(user!!.username)
        user = null
    }

    fun login(username: String, password: String) : Result<LoggedInUser> {
        val result = dataSource.login(username, password)
        if(result is Result.Success)
            user = result.data
        return result
    }

    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String) : Result<Void>{
        return dataSource.register(email, firstName, lastName, imgUrl, username, password)
    }

    fun getContacts() : Result<List<User>>{
        return dataSource.getContacts()
    }

    fun deleteContact(contact: String) : Result<Void>{
        return dataSource.deleteContact(contact)
    }

    fun getUsers() : Result<List<User>>{
        return dataSource.getUsers()
    }

    fun addContact(contact : String) : Result<Void>{
        return dataSource.addContact(contact)
    }

    fun getMeetings() : Result<List<Meeting>>{
        return dataSource.getMeetings()
    }

    fun createMeeting(date: Date, name: String, participants: List<User>): Result<Void>{
        return dataSource.createMeeting(date, name, participants)
    }

    fun getMeetingParticipants(meeting: Meeting) : Result<List<User>>{
        return dataSource.getMeetingParticipants(meeting)
    }

    fun getSettings() : Result<JSONObject> {
        return dataSource.getSettings()
    }

    fun updateSettings(settingsJson : JSONObject) : Result<Void>{
        return dataSource.updateSettings(settingsJson)
    }
}