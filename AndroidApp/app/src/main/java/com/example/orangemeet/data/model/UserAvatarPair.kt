//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class UserAvatarPair(
    val user : User,
    val avatar : Bitmap?
)