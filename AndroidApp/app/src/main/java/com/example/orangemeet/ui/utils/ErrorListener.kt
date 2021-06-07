package com.example.orangemeet.ui.utils

import androidx.annotation.StringRes

interface ErrorListener {
    fun onError(@StringRes error : Int)
}