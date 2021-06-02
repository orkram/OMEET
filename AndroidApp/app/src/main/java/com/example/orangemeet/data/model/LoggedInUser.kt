package com.example.orangemeet.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
class LoggedInUser(val username: String,
                   val password : String,
                   val email: String,
                   var accessToken : String,
                   var refreshToken : String) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!){
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(email)
        parcel.writeString(accessToken)
        parcel.writeString(refreshToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoggedInUser> {
        override fun createFromParcel(parcel: Parcel): LoggedInUser {
            return LoggedInUser(parcel)
        }

        override fun newArray(size: Int): Array<LoggedInUser?> {
            return arrayOfNulls(size)
        }
    }
}