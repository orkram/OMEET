package com.example.orangemeet.ui.main

import com.example.orangemeet.data.model.User

data class GetContactsResult(
    var success : List<User>?,
    var error : Int?
)