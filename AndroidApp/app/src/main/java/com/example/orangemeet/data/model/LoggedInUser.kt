//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
class LoggedInUser(val username: String,
                   val password : String,
                   val email: String,
                   val firstname : String,
                   val lastname : String,
                   var accessToken : String,
                   var refreshToken : String)