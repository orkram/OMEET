package com.example.orangemeet

import java.nio.charset.Charset
import java.util.*

class Util {
    companion object{
        fun generateRandomString(length : Int) : String{
            val array = ByteArray(length)
            Random().nextBytes(array)
            for(i in array.indices){
                if(array[i] < 0)
                    array[i] = (-array[i]).toByte()
                array[i] = (array[i] % 26 + 97).toByte()
            }
            return String(array, Charset.forName("UTF-8"))
        }
    }
}