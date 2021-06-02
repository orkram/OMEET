package com.example.orangemeet.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangemeet.data.BackendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel() : ViewModel() {
    fun logout(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                BackendRepository.logout()
            }
        }
    }
}