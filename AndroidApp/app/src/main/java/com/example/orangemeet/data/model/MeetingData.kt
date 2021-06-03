package com.example.orangemeet.data.model

import com.google.gson.annotations.SerializedName

class MeetingData (
    var date : String = "",
    @SerializedName("idMeeting")
    var meetingId : Long,
    var name : String = "",
    var owner : UserData,
    var roomUrl : String
)