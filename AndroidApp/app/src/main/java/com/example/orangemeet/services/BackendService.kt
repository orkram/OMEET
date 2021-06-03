package com.example.orangemeet.services

import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.*
import com.example.orangemeet.data.model.network.MeetingData
import com.example.orangemeet.data.model.network.UserData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BackendService : DataSource {

    val backendUrl = "http://130.61.186.61:9000"
    lateinit var retroBackendService: RetroBackendService
    private var loggedInUser : LoggedInUser? = null

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(backendUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retroBackendService = retrofit.create(RetroBackendService::class.java)
    }

    private fun getAuthorization() = "Bearer ${loggedInUser!!.accessToken}"

    private fun meetingsFromMeetingDatas(meetingDatas : List<MeetingData>) : List<Meeting>{
        val meetings = mutableListOf<Meeting>()

        meetingDatas.forEach{meetingData ->
            val meeting = Meeting(
                    meetingData.meetingId,
                    meetingData.name,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(meetingData.date),
                    User(meetingData.owner.username, meetingData.owner.email),
                    meetingData.roomUrl
            )
            meetings.add(meeting)
        }

        return meetings
    }

    private fun usersFromUserDatas(userDatas : List<UserData>) : List<User>{
        val users = mutableListOf<User>()
        userDatas.forEach { userData ->
            users.add(User(userData.username, userData.email))
        }
        return users
    }

    private fun refreshAccessToken() : Boolean{
        val response = retroBackendService.refreshAccessToken(
                loggedInUser!!.username,
                JsonObject().apply {
                    addProperty("refreshToken", loggedInUser!!.refreshToken)
                }).execute()
        if(response.isSuccessful) {
            loggedInUser!!.accessToken = response.body()!!.get("accessToken").asString
            return true
        }else{
            return false
        }
    }

    private fun <T : Any, U : Any> requestWithAuthorizationHandling(
            requestMaker : () -> Call<T>,
            success : (response : Response<T>) -> Result<U>,
            error : (response : Response<T>) -> Result<U>) : Result<U> {
        var response = requestMaker().execute()
        if(response.isSuccessful){
            return success(response)
        }else{
            if(response.code() == 401){
                val refreshSuccess = refreshAccessToken()
                if(refreshSuccess){
                    response = requestMaker().execute()
                    if(response.isSuccessful)
                        return success(response)
                    else{
                        val result = login(loggedInUser!!.username, loggedInUser!!.password)
                        if(result is Result.Success){
                            response = requestMaker().execute()
                            if(response.isSuccessful)
                                return success(response)
                        }
                        return error(response)
                    }
                }
            }
            return error(response)
        }
    }

    override fun login(username: String, password: String) : Result<LoggedInUser> {
        val loginJson = com.google.gson.JsonObject()
        loginJson.addProperty("username", username)
        loginJson.addProperty("password", password)

        val request = retroBackendService.login(loginJson)

        val response = request.execute()
        if(response.isSuccessful){
            val loginResult = response.body()!!
            val accessToken = loginResult.get("accessToken").asString
            val refreshToken = loginResult.get("refreshToken").asString
            val loggedInUser = LoggedInUser(username, password, "todo@email.com", accessToken, refreshToken)
            this.loggedInUser = loggedInUser
            return Result.Success(loggedInUser)
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }

    override fun logout(username : String) {
        val response = retroBackendService.logout(username).execute()
    }

    override fun register(
            email: String,
            firstName: String,
            lastName: String,
            imgUrl: String,
            username: String,
            password: String
    ): Result<Nothing> {
        val registerJson = JsonObject()
        registerJson.addProperty("eMail", email)
        registerJson.addProperty("firstName", firstName)
        registerJson.addProperty("lastName", lastName)
        registerJson.addProperty("imgURL", imgUrl)
        registerJson.addProperty("userName", username)
        registerJson.addProperty("password", password)

        val response = retroBackendService.register(registerJson).execute()
        if(response.isSuccessful){
            return Result.Success(null)
        }else
            return Result.Error(IOException("Error code: " + response.code().toString()))
    }

    override fun getContacts(): Result<List<User>> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.getContacts(loggedInUser!!.username, getAuthorization())},
                {response -> Result.Success(usersFromUserDatas(response.body()!!)) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun deleteContact(contact: String): Result<Nothing> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.deleteContact(loggedInUser!!.username, contact, getAuthorization())},
                {response -> Result.Success(null) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun getUsers(): Result<List<User>> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.getUsers(getAuthorization())},
                {response -> Result.Success(usersFromUserDatas(response.body()!!)) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun addContact(contact: String): Result<Nothing> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.addContact(loggedInUser!!.username, contact, getAuthorization())},
                {response -> Result.Success(null) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun getMeetings(): Result<List<Meeting>> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.getMeetings(loggedInUser!!.username, getAuthorization())},
                {response -> Result.Success(meetingsFromMeetingDatas(response.body()!!)) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun createMeeting(date: Date, name: String, participants: List<User>): Result<Nothing> {
        val meetingJson = JsonObject()
        val participantsJson = JsonArray()
        participants.forEach { participant ->
            participantsJson.add(participant.username)
        }
        meetingJson.addProperty("date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
        meetingJson.addProperty("name", name)
        meetingJson.addProperty("ownerUserName", loggedInUser!!.username)
        meetingJson.add("participants", participantsJson)

        val result = requestWithAuthorizationHandling(
                {retroBackendService.createMeeting(meetingJson, getAuthorization())},
                {response -> Result.Success(null) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun getMeetingParticipants(meeting: Meeting): Result<List<User>> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.getMeetingParticipants(meeting.id, getAuthorization())},
                {response -> Result.Success(usersFromUserDatas(response.body()!!)) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun getSettings(): Result<JSONObject> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.getSettings(loggedInUser!!.username, getAuthorization())},
                {response -> Result.Success(JSONObject(response.body().toString())) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun updateSettings(settingsJson: JSONObject): Result<Nothing> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.updateSettings(
                        loggedInUser!!.username,
                        JsonParser.parseString(settingsJson.toString()).asJsonObject,
                        getAuthorization())},
                {response -> Result.Success(null) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }
}