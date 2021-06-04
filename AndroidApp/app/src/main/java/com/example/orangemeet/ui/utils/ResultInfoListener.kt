package com.example.orangemeet.ui.utils

import com.example.orangemeet.data.Result

interface ResultInfoListener <T : Any>{
    fun onResult(resultInfo : ResultInfo<T>)
}