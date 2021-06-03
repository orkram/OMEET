package com.example.orangemeet.ui.main

import com.example.orangemeet.data.model.User

class GetMeetingParticipantsResult(
        var success : List<User>?,
        var error : Int?
)