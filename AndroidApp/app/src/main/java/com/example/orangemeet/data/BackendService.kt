package com.example.orangemeet.data

import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.User
import com.example.orangemeet.services.RetroBackendService
import com.example.orangemeet.services.DataSource
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BackendService : DataSource {
    val backendUrl = "http://130.61.186.61:9000"

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

    override fun getContacts(username: String): Result<List<User>> {
        val response = retroBackendService.getContacts(username, "Bearer " + accessToken).execute()
        if(response.isSuccessful){
            val users = mutableListOf<User>()
            response.body()!!.forEach { userData ->
                users.add(User(userData.username, userData.email))
            }
            return Result.Success(users)
        }else{
            return Result.Error(IOException("Error code: " + response.code().toString()))
        }
    }
}