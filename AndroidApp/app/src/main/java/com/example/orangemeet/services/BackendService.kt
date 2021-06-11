//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.Result
import com.example.orangemeet.data.model.User
import com.google.gson.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class BackendService : DataSource {

    val backendUrl = "http://130.61.186.61:9000"
    val minioUrl = "http://130.61.186.61:9001"
    var retroBackendService : RetroBackendService
    var retroMinioService : RetroMinioService
    private var loggedInUser : LoggedInUser? = null

    private val backendGson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    init {
        val retrofitBackend = Retrofit.Builder()
            .baseUrl(backendUrl)
            .addConverterFactory(GsonConverterFactory.create(backendGson))
            .build()
        retroBackendService = retrofitBackend.create(RetroBackendService::class.java)

        val retrofitMinio = Retrofit.Builder()
                .baseUrl(minioUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        retroMinioService = retrofitMinio.create(RetroMinioService::class.java)
    }

    private fun getAuthorization() = "Bearer ${loggedInUser!!.accessToken}"

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
            loggedInUser = LoggedInUser(username, password, "null", "null", "null", accessToken, refreshToken)
            val getUserResult = getUser(username)
            if (getUserResult is Result.Success) {
                val user = getUserResult.data!!
                loggedInUser = LoggedInUser(username, password, user.email,
                    user.firstname, user.lastname, accessToken, refreshToken)
            }
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
                {response -> Result.Success(response.body()!!) },
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
                {response -> Result.Success(response.body()!!) },
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
                {response -> Result.Success(response.body()!!) },
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
                {response -> Result.Success(response.body()!!) },
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

    fun getImageUpdateUrl(username : String) : Result<String> {
        val result = requestWithAuthorizationHandling(
                {retroMinioService.getImageUpdateUrl(username, getAuthorization())},
                {response -> Result.Success(response.body()!!.get("imgUpdateUrl").asString) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun getUser(username: String) : Result<User> {
        val result = requestWithAuthorizationHandling(
                {retroBackendService.getUser(username, getAuthorization())},
                {response -> Result.Success(response.body()!!) },
                {response -> Result.Error(IOException("Error code: " + response.code().toString())) }
        )
        return result
    }

    override fun getAvatar(imgUrl : String) : Result<Bitmap> {
        try {
            val image = BitmapFactory.decodeStream(URL(imgUrl).openConnection().getInputStream())
            return Result.Success(image)
        } catch (ex : IOException) {
            return Result.Error(IOException("Error downloading image: " + ex.message))
        }
    }
}