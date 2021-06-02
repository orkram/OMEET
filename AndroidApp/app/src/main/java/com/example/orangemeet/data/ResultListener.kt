package com.example.orangemeet.data

import com.example.orangemeet.data.model.LoggedInUser
import kotlin.Result

interface ResultListener<T : Any> {
    fun onResult(result : T)
}