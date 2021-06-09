package com.example.orangemeet.ui.utils

interface ResultInfoListener <T : Any>{
    fun onResult(resultInfo : ResultInfo<T>)
}