package com.example.orangemeet.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class UserAvatarPair(
    val user : User,
    val avatar : Bitmap?
)