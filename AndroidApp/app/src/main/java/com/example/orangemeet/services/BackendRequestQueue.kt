package com.example.orangemeet.services

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import java.lang.IllegalStateException

class BackendRequestQueue(context : Context) {
    companion object {
        @Volatile
        private var instance: BackendRequestQueue? = null
        fun getInstance(context: Context) =
                instance ?: synchronized(this) {
                    instance ?: BackendRequestQueue(context).also {
                        instance = it
                    }
                }

        fun getInstance() : BackendRequestQueue{
            if (null == instance) {
                throw IllegalStateException(BackendRequestQueue::class.java.getSimpleName() +
                        " is not initialized, call getInstance(...) with context first")
            }
            return instance!!
        }
    }



    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}