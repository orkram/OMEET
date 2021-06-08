package com.example.orangemeet.ui.main

import androidx.lifecycle.ViewModel
import com.example.orangemeet.data.DataRepository

class HomeViewModel : ViewModel() {

    fun getGreeting() : String {
        return "Witaj " + DataRepository.loggedInUser!!.firstname + " " + DataRepository.loggedInUser!!.lastname + "!"
    }
}