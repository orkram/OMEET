//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.example.orangemeet.ui.main

import androidx.lifecycle.ViewModel
import com.example.orangemeet.data.DataRepository

class HomeViewModel : ViewModel() {

    fun getGreeting() : String {
        return "Witaj " + DataRepository.loggedInUser!!.firstname + " " + DataRepository.loggedInUser!!.lastname + "!"
    }
}