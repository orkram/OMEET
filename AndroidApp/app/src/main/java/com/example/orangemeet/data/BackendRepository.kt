package com.example.orangemeet.data

import com.example.orangemeet.data.model.LoggedInUser
import com.example.orangemeet.data.model.User
import com.example.orangemeet.services.DataSource

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

object BackendRepository {

    val dataSource : DataSource = BackendService()

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    fun logout() {
        if(user == null)
            return

        /*val logoutUrl = BackendCommunication.backendUrl + "/api/v1/account/" + user!!.username + "/logout"

        val logoutRequest = JsonObjectRequest(
                Request.Method.POST, logoutUrl, JSONObject(),
                Response.Listener {response ->
                    Timber.i("Logout success")
                },
                Response.ErrorListener {error ->
                    Timber.e("Logout failed")
                }
        )

        BackendRequestQueue.getInstance().requestQueue.add(logoutRequest)*/

        dataSource.logout(user!!.username)
        user = null
    }

    fun login(username: String, password: String) : Result<LoggedInUser> {
        val result = dataSource.login(username, password)
        if(result is Result.Success)
            user = result.data
        return result
    }

    fun register(email : String, firstName : String, lastName : String,
                 imgUrl : String, username : String, password : String) : Result<Void>{
        return dataSource.register(email, firstName, lastName, imgUrl, username, password)
    }

    fun getContacts() : Result<List<User>>{
        return dataSource.getContacts(user!!.username)
    }

}