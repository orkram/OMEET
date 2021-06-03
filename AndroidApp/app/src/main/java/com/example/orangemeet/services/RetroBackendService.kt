package com.example.orangemeet.services

import com.example.orangemeet.data.model.network.MeetingData
import com.example.orangemeet.data.model.network.UserData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetroBackendService{
    @POST("api/v1/account/login")
    fun login(@Body loginJson : JsonObject) : Call<JsonObject>

    @POST("api/v1/account/{username}/logout")
    fun logout(@Path("username") username : String) : Call<Void>

    @POST("api/v1/account/register")
    fun register(@Body registerJson : JsonObject) : Call<Void>

    @GET("api/v1/contacts/friends/{username}?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true")
    fun getContacts(@Path("username") username: String, @Header("authorization") authorization : String) : Call<List<UserData>>

    @DELETE("api/v1/contacts/friends/{username}")
    fun deleteContact(@Path("username") username: String, @Query("friend") friend : String, @Header("authorization") authorization : String) : Call<Void>

    @GET("api/v1/users")
    fun getUsers(@Header("authorization") authorization : String) : Call<List<UserData>>

    @POST("api/v1/contacts/add")
    fun addContact(@Query("user-f") username: String, @Query("user-o") contact : String, @Header("authorization") authorization : String) : Call<Void>

    @GET("api/v1/meetings/participants/user/{username}?meetingNameSortAscending=true")
    fun getMeetings(@Path("username") username : String, @Header("authorization") authorization : String) : Call<List<MeetingData>>

    @POST("api/v1/meetings")
    fun createMeeting(@Body meetingJson : JsonObject, @Header("authorization") authorization : String) : Call<Void>

    @GET("api/v1/meetings/participants/meeting/{meetingId}?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true")
    fun getMeetingParticipants(@Path("meetingId") meetingId : Long, @Header("authorization") authorization : String) : Call<List<UserData>>

    @GET("api/v1/users/settings/{username}")
    fun getSettings(@Path("username") username : String, @Header("authorization") authorization : String) : Call<JsonObject>

    @PUT("api/v1/users/settings/{username}")
    fun updateSettings(@Path("username") username: String, @Body settingsJson : JsonObject, @Header("authorization") authorization : String) : Call<Void>

    @POST("api/v1/account/{username}/refresh-token")
    fun refreshAccessToken(@Path("username") username: String, @Body refreshTokenJson : JsonObject) : Call<JsonObject>
}