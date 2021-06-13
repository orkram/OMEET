//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.data.model

import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.orangemeet.R
import com.example.orangemeet.utils.Util
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.lang.UnsupportedOperationException


data class User(
        @SerializedName("userName")
        val username : String,
        @SerializedName("eMail")
        val email : String,
        @SerializedName("firstName")
        val firstname : String,
        @SerializedName("lastName")
        val lastname : String,
        @SerializedName("imgURL")
        val imgUrl : String) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!)

    override fun equals(other: Any?): Boolean {
        if(!(other is User))
            return false

        val otherUser = other as User
        return this.username == otherUser.username &&
                this.email == otherUser.email &&
                this.firstname == otherUser.firstname &&
                this.lastname == otherUser.lastname
        //Imgurl can be different
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(imgUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object{

        val CREATOR = object : Parcelable.Creator<User>{
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                throw UnsupportedOperationException()
            }
        }

        fun fromJson(jsonObject: JSONObject) : User {
            return User(
                    jsonObject.getString("userName"),
                    jsonObject.getString("eMail"),
                    jsonObject.getString("firstName"),
                    jsonObject.getString("lastName"),
                    jsonObject.getString("imgURL"))
        }

    }
}