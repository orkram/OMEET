package com.example.orangemeet

import android.app.Application
import com.example.orangemeet.services.BackendRequestQueue
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG)
            Timber.plant(DebugTree())
        BackendRequestQueue.getInstance(this)
    }
}