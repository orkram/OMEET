package com.example.orangemeet.ui.utils

class ResultInfo<T : Any>(
    var success : Boolean,
    var data : T? = null,
    var error : Int? = null
)