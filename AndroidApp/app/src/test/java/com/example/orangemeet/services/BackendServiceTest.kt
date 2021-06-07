package com.example.orangemeet.services

import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.Result
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class BackendServiceTest {

    val service = BackendService()

    val REQUEST_FAIL = "Request failed"

    lateinit var loggedInUser : LoggedInUser

    @Before
    fun setUp() {
        val loginResult = service.login("konrads", "konrads123")
        if (loginResult is Result.Success) {
            loggedInUser = loginResult.data!!
            print("LoggedIn")
        } else {
            fail("Couldn't login")
        }
    }

    @After
    fun tearDown() {
        service.logout(loggedInUser.username)
    }

    @Test
    fun getImageUrl() {
        val result = service.getImageUrl(loggedInUser.username)
        if (result is Result.Success) {
            val url = result.data!!
            if (url.isEmpty()) {
                fail("Image URL is empty")
            } else {
                print("Image URL: " + url)
            }
        } else {
            fail(REQUEST_FAIL)
        }
    }
}