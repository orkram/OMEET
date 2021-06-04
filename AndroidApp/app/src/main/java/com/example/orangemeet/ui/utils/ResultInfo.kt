package com.example.orangemeet.ui.utils

import androidx.annotation.StringRes

class ResultInfo<T : Any>(
    var success : Boolean,
    var data : T? = null,
    @StringRes var error : Int? = null
)