package com.example.orangemeet.ui.main

import com.example.orangemeet.data.model.Meeting

data class GetMeetingsResult(
    var success : List<Meeting>?,
    var error : Int?
)