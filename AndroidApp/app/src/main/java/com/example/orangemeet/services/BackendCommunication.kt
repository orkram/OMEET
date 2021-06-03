//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.orangemeet.UserInfo
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.data.model.RemoteSettings
import com.example.orangemeet.data.model.User
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class BackendCommunication {
    companion object{
        private const val backendUrl = "http://130.61.186.61:9000"

        private var username : String? = null
        private var password : String? = null
        private var accessToken : String? = null
        private var refreshToken : String? = null

        private fun refreshAccessToken(context : Context,
                                       listener: Response.Listener<JSONObject>?,
                                       errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

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

        fun login(context : Context, username : String, password : String, listener: Response.Listener<JSONObject>?,
                  errorListener: Response.ErrorListener?){

            val requestQueue = Volley.newRequestQueue(context)

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

        fun logout(context : Context, onLogout: () -> Unit){

            if(username == null)
                return

            val requestQueue = Volley.newRequestQueue(context)

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

        fun register(context : Context, email : String, firstName : String, lastName : String,
                     imgUrl : String, username : String, password : String,
                     listener: Response.Listener<JSONObject>?,
                     errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

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
                context: Context,
                error : VolleyError,
                listener: Response.Listener<JSONObject>?,
                errorListener: Response.ErrorListener?)
        {
            if(error.networkResponse.statusCode == 401){
                refreshAccessToken(
                        context,
                        Response.Listener {
                            listener?.onResponse(it)
                        },
                        Response.ErrorListener { refreshError ->
                            if (refreshError.networkResponse.statusCode == 401) {
                                login(
                                        context,
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

        fun getContactsList(context: Context, listener: Response.Listener<List<User>>?,
                            errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(
                    Request.Method.GET,
                    backendUrl + "/api/v1/contacts/friends/" + username +
                            "?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true",
                    JSONObject(),
                    Response.Listener { jsonArray ->
                        Timber.i("GetContactsList success")
                        val contactsList = mutableListOf<User>()
                        for (i in 0 until jsonArray.length())
                            contactsList.add(User.createFromJson(jsonArray.getJSONObject(i)))
                        listener?.onResponse(contactsList)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    getContactsList(
                                            context,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("GetContactsList failed: %s", error.message)
                                    errorListener?.onErrorResponse(error)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun deleteContact(context: Context, friend: String, listener: Response.Listener<JSONObject>?,
                          errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(
                    Request.Method.DELETE,
                    backendUrl + "/api/v1/contacts/friends/" + username + "?friend=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Timber.i("DeleteContact success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    deleteContact(
                                            context,
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

        fun getUsers(context: Context, query : String?, listener: Response.Listener<List<User>>?,
                     errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(
                    Request.Method.GET,
                    backendUrl + "/api/v1/users",
                    JSONObject(),
                    Response.Listener { jsonArray ->
                        Timber.i("GetUsers success")
                        val contacts = mutableListOf<User>()
                        for (i in 0 until jsonArray.length()) {
                            contacts.add(i, User.createFromJson(jsonArray.getJSONObject(i)))
                        }
                        listener!!.onResponse(contacts)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    getUsers(
                                            context,
                                            query,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("GetUsers failed: %s", it.message)
                                    errorListener!!.onErrorResponse(it)
                                })

                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun addContact(context: Context, friend: String, listener: Response.Listener<JSONObject>?,
                       errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(
                    Request.Method.POST,
                    backendUrl + "/api/v1/contacts/add" + "?user-f=" + username + "&user-o=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Timber.i("AddContact success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    addContact(
                                            context,
                                            friend,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("AddContact failed: %s", it.message)
                                    errorListener!!.onErrorResponse(it)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun sendInvite(context: Context, friend: String, listener: Response.Listener<JSONObject>?,
                       errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(
                    Request.Method.POST,
                    backendUrl + "/api/v1/contacts/send-invite" + "?from=" + username + "&to=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Timber.i("SendInvite success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    sendInvite(
                                            context,
                                            friend,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("SendInvite failed: %s", it.message)
                                    errorListener!!.onErrorResponse(it)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun getMeetings(context: Context, listener: Response.Listener<List<Meeting>>?,
                        errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(
                    Request.Method.GET,
                    backendUrl + "/api/v1/meetings/participants/user/" + username + "?meetingNameSortAscending=true",
                    JSONObject(),
                    Response.Listener { jsonArray ->
                        Timber.i("GetMeetings success")
                        val meetings = mutableListOf<Meeting>()
                        for (i in 0 until jsonArray.length())
                            meetings.add(Meeting.createFromJson(jsonArray.getJSONObject(i)))
                        listener!!.onResponse(meetings)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    getMeetings(
                                            context,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("GetMeetings failed: %s", it.message)
                                    errorListener!!.onErrorResponse(it)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun createMeeting(context: Context, date : Date, name : String, participants : List<User>,
                          listener: Response.Listener<JSONObject>?,
                          errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val participantsJsonArray = JSONArray()
            participants.forEach{participant -> participantsJsonArray.put(participant.username)}

            val meetingJson = JSONObject()
                    .put("date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
                    .put("name", name)
                    .put("ownerUserName",
                            username
                    )
                    .put("participants", participantsJsonArray)

            val request = BackendRequestJsonObject(
                    Request.Method.POST,
                    backendUrl + "/api/v1/meetings",
                    meetingJson,
                    Response.Listener { jsonObject ->
                        Timber.i("CreateMeeting success")
                        listener?.onResponse(jsonObject)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    createMeeting(
                                            context,
                                            date,
                                            name,
                                            participants,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("CreateMeeting failed: %s", error.message)
                                    errorListener?.onErrorResponse(error)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun getMeetingParticipants(context: Context, meetingId : Long,
                                   listener: Response.Listener<List<User>>?,
                                   errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(
                    Request.Method.GET,
                    backendUrl + "/api/v1/meetings/participants/meeting/" + meetingId +
                            "?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true",
                    JSONObject(),
                    Response.Listener { jsonArray ->
                        Timber.i("GetMeetingParticipants success")
                        val contacts = mutableListOf<User>()
                        for (i in 0 until jsonArray.length())
                            contacts.add(User.createFromJson(jsonArray.getJSONObject(i)))
                        listener?.onResponse(contacts)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    getMeetingParticipants(
                                            context,
                                            meetingId,
                                            listener,
                                            errorListener
                                    )
                                },
                                Response.ErrorListener {
                                    Timber.e("GetMeetingParticipants failed: %s", error.message)
                                    errorListener?.onErrorResponse(error)
                                })
                    },
                    accessToken
            )

            requestQueue.add(request)
        }

        fun getSettings(context: Context,
                        listener: Response.Listener<JSONObject>?,
                        errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(
                    Request.Method.GET,
                    backendUrl + "/api/v1/users/settings/" + username,
                    JSONObject(),
                    Response.Listener {
                        Timber.i("GetSettings success")
                        listener?.onResponse(it)
                    },
                    Response.ErrorListener { error ->
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    getSettings(
                                            context,
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

        fun updateSettings(context: Context, newSettings : RemoteSettings,
                           listener: Response.Listener<JSONObject>?,
                           errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

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
                        handleAuthorizationError(context,
                                error,
                                Response.Listener {
                                    updateSettings(
                                            context,
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