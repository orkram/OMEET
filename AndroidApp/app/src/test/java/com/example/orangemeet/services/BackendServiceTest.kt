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
    val LOGIN = "arkadzi.zaleuski"
    val PASSWORD = "admin123!"
    /*val LOGIN = "konrads"
    val PASSWORD = "konrads123"*/

    lateinit var loggedInUser : LoggedInUser

    @Before
    fun setUp() {
        val loginResult = service.login(LOGIN, PASSWORD)
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
    fun getImageUpdateUrl() {
        val result = service.getImageUpdateUrl(loggedInUser.username)
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

    @Test
    fun getUser() {
        val result = service.getUser(loggedInUser.username)
        if (result is Result.Success) {
            print(result.data!!)
        } else {
            fail(REQUEST_FAIL)
        }
    }

    @Test
    fun getImage() {
        val getUserResult = service.getUser(loggedInUser.username)
        if (getUserResult is Result.Success) {
            val user = getUserResult.data!!
            val getImageResult = service.getAvatar(user.imgUrl)
            if (getImageResult is Result.Success) {
                print("Image json:")
                print(getImageResult.data!!)
            } else {
                print(getImageResult.toString())
                fail(REQUEST_FAIL)
            }
        } else {
            fail(REQUEST_FAIL)
        }
    }
}