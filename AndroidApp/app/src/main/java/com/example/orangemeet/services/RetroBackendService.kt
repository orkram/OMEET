package com.example.orangemeet.services

import com.example.orangemeet.data.model.LoginResponse
import com.example.orangemeet.data.model.UserData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetroBackendService{
    @POST("api/v1/account/login")
    fun login(@Body loginJson : JsonObject) : Call<LoginResponse>

    @POST("api/v1/account/{username}/logout")
    fun logout(@Path("username") username : String) : Call<Void>

    @POST("api/v1/account/register")
    fun register(@Body registerJson : JsonObject) : Call<Void>

    @GET("api/v1/contacts/friends/{username}?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true")
    @Headers("accept: */*")
    fun getContacts(@Path("username") username: String, @Header("authorization") authorization : String) : Call<List<UserData>>
}