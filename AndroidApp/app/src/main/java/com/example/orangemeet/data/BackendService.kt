package com.example.orangemeet.data

import com.example.orangemeet.data.model.*
import com.example.orangemeet.services.DataSource
import com.example.orangemeet.services.RetroBackendService
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BackendService : DataSource {
    val backendUrl = "http://130.61.186.61:9000"

    var username : String = ""
    var accessToken : String = ""
    var refreshToken : String = ""


    lateinit var retroBackendService: RetroBackendService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://130.61.186.61:9000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retroBackendService = retrofit.create(RetroBackendService::class.java)
    }

    private fun getAuthorization() = "Bearer $accessToken"

    override fun login(username: String, password: String) : Result<LoggedInUser> {
        val loginJson = com.google.gson.JsonObject()
        loginJson.addProperty("username", username)
        loginJson.addProperty("password", password)

        val request = retroBackendService.login(loginJson)

        val response = request.execute()
        if(response.isSuccessful){
            val loginResult = response.body()!!
            accessToken = loginResult.accessToken
            refreshToken = loginResult.refreshToken
            this.username = username
            val loggedInUser = LoggedInUser(username, password, "todo", loginResult.accessToken, loginResult.refreshToken)
            return Result.Success(loggedInUser)
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }

       /* val loginUrl = backendUrl + "/api/v1/account/login"

        val loginJson = JSONObject()
                .put("username", username)
                .put("password", password)

        val loginRequest = JsonObjectRequest(
                Request.Method.POST, loginUrl, loginJson,
                Response.Listener { response ->
                    Timber.i("Login success")
                    UserInfo.userName = username
                    val loggedInUser = LoggedInUser(
                            username,
                            password,
                            "todo",
                            response.getString("accessToken"),
                            response.getString("refreshToken"))
                    listener.onResult(Result.Success(loggedInUser))
                },
                Response.ErrorListener { error ->
                    Timber.e("Login failed")
                    listener.onResult(Result.Error(IOException("Error logging in")))
                }
        )*/
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
    ) : Result<Void> {
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

    private fun usersFromUserDatas(userDatas : List<UserData>) : List<User>{
        val users = mutableListOf<User>()
        userDatas.forEach { userData ->
            users.add(User(userData.username, userData.email))
        }
        return users
    }

    override fun getContacts(): Result<List<User>> {
        val response = retroBackendService.getContacts(username, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(usersFromUserDatas(response.body()!!))
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }

    override fun deleteContact(contact: String): Result<Void> {
        val response = retroBackendService.deleteContact(username, contact, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(null)
        }else
            return Result.Error(IOException("Error code: " + response.code().toString()))
    }

    override fun getUsers(): Result<List<User>> {
        val response = retroBackendService.getUsers(getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(usersFromUserDatas(response.body()!!))
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }

    override fun addContact(contact: String): Result<Void> {
        val response = retroBackendService.addContact(username, contact, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(null)
        }else
            return Result.Error(IOException("Error code: " + response.code().toString()))
    }

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

    override fun getMeetings(): Result<List<Meeting>> {
        val response = retroBackendService.getMeetings(username, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(meetingsFromMeetingDatas(response.body()!!))
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }

    override fun createMeeting(date: Date, name: String, participants: List<User>): Result<Void> {
        val meetingJson = JsonObject()
        val participantsJson = JsonArray()
        participants.forEach { participant ->
            participantsJson.add(participant.username)
        }
        meetingJson.addProperty("date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
        meetingJson.addProperty("name", name)
        meetingJson.addProperty("ownerUserName", username)
        meetingJson.add("participants", participantsJson)

        val response = retroBackendService.createMeeting(meetingJson, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(null)
        }else
            return Result.Error(IOException("Error code: " + response.code().toString()))
    }

    override fun getMeetingParticipants(meeting: Meeting): Result<List<User>> {
        val response = retroBackendService.getMeetingParticipants(meeting.id, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(usersFromUserDatas(response.body()!!))
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }

    override fun getSettings(): Result<JSONObject> {
        val response = retroBackendService.getSettings(username, getAuthorization()).execute()
        if(response.isSuccessful){
            return Result.Success(JSONObject(response.body().toString()))
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }

    override fun updateSettings(settingsJson: JSONObject): Result<Void> {
        val response = retroBackendService.updateSettings(
                username,
                JsonParser.parseString(settingsJson.toString()).asJsonObject,
                getAuthorization()).execute()

        if(response.isSuccessful){
            return Result.Success(null)
        }else
            return Result.Error(IOException("Error code: " + response.code().toString()))
    }
}