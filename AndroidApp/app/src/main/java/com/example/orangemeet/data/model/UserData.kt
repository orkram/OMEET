package com.example.orangemeet.data.model

import com.google.gson.annotations.SerializedName

class UserData {
    @SerializedName("userName")
    var username : String = ""

    @SerializedName("eMail")
    var email : String = ""

    @SerializedName("firstName")
    var firstname : String = ""

    @SerializedName("lastName")
    var lastname : String = ""

    @SerializedName("imgURL")
    var imgUrl : String = ""
}