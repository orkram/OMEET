package com.example.orangemeet.services

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.orangemeet.UserInfo
import com.example.orangemeet.data.model.RemoteSettings
import org.json.JSONObject
import timber.log.Timber

class BackendCommunication {
    companion object{
        val backendUrl = "http://130.61.186.61:9000"

        var username : String? = null
        var password : String? = null
        var accessToken : String? = null
        var refreshToken : String? = null

        private fun refreshAccessToken(requestQueue : RequestQueue,
                                       listener: Response.Listener<JSONObject>?,
                                       errorListener: Response.ErrorListener?){

            val tokenUrl = backendUrl + "/api/v1/account/" + username + "/refresh-token"

            val refreshTokenJsonObject = JSONObject().put("refreshToken",
                    refreshToken
            )

            val tokenRequest = BackendRequestJsonObject(
                    Request.Method.POST, tokenUrl,
                    refreshTokenJsonObject,
                    Response.Listener {
                        Timber.i("RefreshAccessToken success")
                        accessToken =
                                it.getString("accessToken")
                        listener?.onResponse(it)
                    },
                    Response.ErrorListener {
                        Timber.e("RefreshAccessToken failed")
                        errorListener?.onErrorResponse(it)
                    },
                    null
            )

            requestQueue.add(tokenRequest)
        }

        fun login(requestQueue: RequestQueue, username : String, password : String, listener: Response.Listener<JSONObject>?,
                  errorListener: Response.ErrorListener?){

            val loginUrl = backendUrl + "/api/v1/account/login"

            val loginJson = JSONObject()
                .put("username", username)
                .put("password", password)

            val loginRequest = JsonObjectRequest(
                    Request.Method.POST, loginUrl, loginJson,
                    Response.Listener {response ->
                        Timber.i("Login success")
                        Companion.username = username
                        Companion.password = password
                        accessToken = response.getString("accessToken")
                        refreshToken = response.getString("refreshToken")
                        listener?.onResponse(response)
                    },
                    Response.ErrorListener {error ->
                        Timber.e("Login failed")
                        errorListener?.onErrorResponse(error)
                    }
            )

            requestQueue.add(loginRequest)
            UserInfo.userName = username
        }

        fun logout(requestQueue: RequestQueue, onLogout: () -> Unit){

            if(username == null)
                return

            val logoutUrl = backendUrl + "/api/v1/account/" + username + "/logout"

            val logoutRequest = JsonObjectRequest(
                    Request.Method.POST, logoutUrl, JSONObject(),
                    Response.Listener {response ->
                        Timber.i("Logout success")
                        onLogout()
                    },
                    Response.ErrorListener {error ->
                        Timber.e("Logout failed")
                        onLogout()
                    }
            )

            username = null
            password = null

            requestQueue.add(logoutRequest)
        }

        fun register(requestQueue: RequestQueue, email : String, firstName : String, lastName : String,
                     imgUrl : String, username : String, password : String,
                     listener: Response.Listener<JSONObject>?,
                     errorListener: Response.ErrorListener?){

            val registerUrl = backendUrl + "/api/v1/account/register"

            val registerJson = JSONObject()
                    .put("eMail", email)
                    .put("firstName", firstName)
                    .put("lastName", lastName)
                    .put("imgURL", imgUrl)
                    .put("userName", username)
                    .put("password", password)

            val registerRequest =
                    BackendRequestJsonObject(
                            Request.Method.POST, registerUrl, registerJson,
                            Response.Listener { response ->
                                Timber.i("Register success")
                                listener?.onResponse(response)
                            },
                            Response.ErrorListener { error ->
                                Timber.e("Register failed: %s", error.message)
                                errorListener?.onErrorResponse(error)
                            },
                            null
                    )

            requestQueue.add(registerRequest)
        }

        private fun handleAuthorizationError(
                requestQueue: RequestQueue,
                error : VolleyError,
                listener: Response.Listener<JSONObject>?,
                errorListener: Response.ErrorListener?)
        {
            if(error.networkResponse.statusCode == 401){
                refreshAccessToken(
                        requestQueue,
                        Response.Listener {
                            listener?.onResponse(it)
                        },
                        Response.ErrorListener { refreshError ->
                            if (refreshError.networkResponse.statusCode == 401) {
                                login(
                                        requestQueue,
                                        username!!,
                                        password!!,
                                        Response.Listener {
                                            listener?.onResponse(it)
                                        },
                                        Response.ErrorListener {
                                            errorListener?.onErrorResponse(it)
                                        })
                            }
                        })
            }
        }

        fun deleteContact(requestQueue: RequestQueue, friend: String, listener: Response.Listener<JSONObject>?,
                          errorListener: Response.ErrorListener?){

            val request = BackendRequestJsonObject(
                    Request.Method.DELETE,
                    backendUrl + "/api/v1/contacts/friends/" + username + "?friend=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Timber.i("DeleteContact success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(requestQueue,
                                error,
                                Response.Listener {
                                    deleteContact(
                                            requestQueue,
                                            friend,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("DeleteContact failed: %s", it.message)
                                    errorListener!!.onErrorResponse(it)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }


        fun getSettings(requestQueue: RequestQueue,
                        listener: Response.Listener<JSONObject>?,
                        errorListener: Response.ErrorListener?){

            val request = BackendRequestJsonObject(
                    Request.Method.GET,
                    backendUrl + "/api/v1/users/settings/" + username,
                    JSONObject(),
                    Response.Listener {
                        Timber.i("GetSettings success")
                        listener?.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(requestQueue,
                                error,
                                Response.Listener {
                                    getSettings(
                                            requestQueue,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("GetSettings failed: %s", error.message)
                                    errorListener?.onErrorResponse(error)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun updateSettings(requestQueue: RequestQueue, newSettings : RemoteSettings,
                           listener: Response.Listener<JSONObject>?,
                           errorListener: Response.ErrorListener?){

            val settingsJson = JSONObject()
                    .put("isDefaultCamOn", newSettings.startWithCam)
                    .put("isDefaultMicOn", newSettings.startWithMic)
                    .put("isPrivate", newSettings.privateUser)

            val request = BackendRequestJsonObject(
                    Request.Method.PUT,
                    backendUrl + "/api/v1/users/settings/" + username,
                    settingsJson,
                    Response.Listener {
                        Timber.i("UpdateSettings success")
                        listener?.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(requestQueue,
                                error,
                                Response.Listener {
                                    updateSettings(
                                            requestQueue,
                                            newSettings,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("UpdateSettings failed: %s", error.message)
                                    errorListener?.onErrorResponse(error)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }
    }
}