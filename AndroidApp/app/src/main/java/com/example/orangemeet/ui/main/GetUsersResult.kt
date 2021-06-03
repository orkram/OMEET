package com.example.orangemeet.ui.main

import com.example.orangemeet.data.model.User

data class GetUsersResult(
    var success : List<User>?,
    var error : Int?
)