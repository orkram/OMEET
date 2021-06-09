package com.example.orangemeet.services

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetroMinioService {
    @GET("api/v1/users/{username}/avatar/update")
    fun getImageUpdateUrl(@Path("username") username: String, @Header("authorization") authorization : String) : Call<JsonObject>

    @GET
    fun getImage(@Url imageUrl: String) : Call<String>
}