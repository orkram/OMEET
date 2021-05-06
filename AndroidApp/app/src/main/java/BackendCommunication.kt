import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.orangemeet.Contact
import com.example.orangemeet.Meeting
import com.example.orangemeet.R
import com.example.orangemeet.userInfo
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BackendCommunication {
    companion object{
        val backendUrl = "http://130.61.186.61:9000"

        private var username : String? = null
        private var token : String? = null

        fun GetToken() : String?{
            return token
        }

        fun Login(context : Context, username : String, password : String, listener: Response.Listener<JSONObject>?,
                  errorListener: Response.ErrorListener?){

            val requestQueue = Volley.newRequestQueue(context)

            val loginUrl = backendUrl + "/api/v1/account/login"

            var loginJson = JSONObject()
                .put("username", username)
                .put("password", password)

            val loginRequest = JsonObjectRequest(
                    Request.Method.POST, loginUrl, loginJson,
                    Response.Listener {response ->
                        Log.i("BackendCommunication", "Login success")
                        this.username = username
                        token = response.getString("accessToken")
                        listener?.onResponse(response)
                    },
                    Response.ErrorListener {error ->
                        Log.e("BackendCommunication", "Register failed: " + error.message)
                        errorListener?.onErrorResponse(error)
                    }
            )

            requestQueue.add(loginRequest)
            userInfo.userName = username;
        }

        fun Register(context : Context, email : String, firstName : String, lastName : String,
                     imgUrl : String, username : String, password : String,
                     listener: Response.Listener<JSONObject>?,
                     errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val registerUrl = backendUrl + "/api/v1/account/register"

            var registerJson = JSONObject()
                    .put("eMail", email)
                    .put("firstName", firstName)
                    .put("lastName", lastName)
                    .put("imgURL", imgUrl)
                    .put("userName", username)
                    .put("password", password)

            val registerRequest = BackendRequestJsonObject(
                    Request.Method.POST, registerUrl, registerJson,
                    Response.Listener {response ->
                        Log.i("BackendCommunication", "Register success")
                        listener?.onResponse(response)
                    },
                    Response.ErrorListener {error ->
                        Log.e("BackendCommunication", "Register failed: " + error.message)
                        errorListener?.onErrorResponse(error)
                    },
                    null)

            requestQueue.add(registerRequest)
        }

        fun GetContactsList(context: Context, listener: Response.Listener<List<Contact>>?,
                            errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(Request.Method.GET, backendUrl + "/api/v1/contacts/friends/" + username +
                    "?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true",
                    JSONObject(), Response.Listener { jsonArray ->
                Log.i("BackendCommunication", "GetContactsList success")
                val contactsList = mutableListOf<Contact>()
                for(i in 0..jsonArray.length() - 1)
                    contactsList.add(Contact.createFromJson(jsonArray.getJSONObject(i)))
                listener?.onResponse(contactsList) },
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "GetContactsList failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun DeleteContact(context: Context, friend: String, listener: Response.Listener<JSONObject>?,
                          errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(Request.Method.DELETE, backendUrl + "/api/v1/contacts/friends/" + username + "?friend=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Log.i("BackendCommunication", "DeleteContact success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "DeleteContact failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun GetUsers(context: Context, query : String?, listener: Response.Listener<List<Contact>>?,
                     errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(Request.Method.GET, backendUrl + "/api/v1/users",
                    JSONObject(),
                    Response.Listener { jsonArray ->
                        Log.i("BackendCommunication", "GetUsers success")
                        val contacts = mutableListOf<Contact>()
                        for(i in 0..jsonArray.length() - 1){
                            contacts.add(i, Contact.createFromJson(jsonArray.getJSONObject(i)))
                        }
                        listener!!.onResponse(contacts)
                    },
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "GetUsers failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun AddContact(context: Context, friend: String, listener: Response.Listener<JSONObject>?,
                          errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(Request.Method.POST,
                    backendUrl + "/api/v1/contacts/add" + "?user-f=" + username + "&user-o=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Log.i("BackendCommunication", "AddContact success")
                        listener!!.onResponse(it)
                    } ,
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "AddContact failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun SendInvite(context: Context, friend: String, listener: Response.Listener<JSONObject>?,
                       errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonObject(Request.Method.POST,
                    backendUrl + "/api/v1/contacts/add" + "?from=" + username + "&to=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Log.i("BackendCommunication", "SendInvite success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "SendInvite failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun GetMeetings(context: Context, listener: Response.Listener<List<Meeting>>?,
                            errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(Request.Method.GET,
                    backendUrl + "/api/v1/meetings/participants/user/" + username + "?meetingNameSortAscending=true",
                    JSONObject(),
                    Response.Listener {jsonArray ->
                        Log.i("BackendCommunication", "GetMeetings success")
                        val meetings = mutableListOf<Meeting>()
                        for(i in 0..jsonArray.length() - 1)
                            meetings.add(Meeting.createFromJson(jsonArray.getJSONObject(i)))
                        listener!!.onResponse(meetings)
                    },
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "GetMeetings failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun CreateMeeting(context: Context, date : Date, name : String, participants : List<Contact>,
                          listener: Response.Listener<JSONObject>?,
                          errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val participantsJsonArray = JSONArray()
            participants.forEach{participant -> participantsJsonArray.put(participant.username)}

            val meetingJson = JSONObject()
                    .put("date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
                    .put("name", name)
                    .put("ownerUserName", username)
                    .put("participants", participantsJsonArray)

            val request = BackendRequestJsonObject(Request.Method.POST,
                    backendUrl + "/api/v1/meetings",
                    meetingJson,
                    Response.Listener {jsonObject ->
                        Log.i("BackendCommunication", "CreateMeeting success")
                        listener?.onResponse(jsonObject)
                    },
                    Response.ErrorListener {error ->
                        Log.e("BackendCommunication", "CreateMeeting failed: " + error.message)
                        errorListener?.onErrorResponse(error)
                    },
                    token)

            requestQueue.add(request)
        }

        fun GetMeetingParticipants(context: Context, meetingId : Long,
                                   listener: Response.Listener<List<Contact>>?,
                                   errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(Request.Method.GET,
                    backendUrl + "/api/v1/meetings/participants/meeting/" + meetingId +
                            "?firstNameSortAscending=true&lastNameSortAscending=true&userNameSortAscending=true",
                    JSONObject(),
                    Response.Listener {jsonArray ->
                        Log.i("BackendCommunication", "GetMeetingParticipants success")
                        val contacts = mutableListOf<Contact>()
                        for(i in 0..jsonArray.length() - 1)
                            contacts.add(Contact.createFromJson(jsonArray.getJSONObject(i)))
                        listener?.onResponse(contacts)
                    },
                    Response.ErrorListener {error ->
                        Log.e("BackendCommunication", "GetMeetingParticipants failed: " + error.message)
                        errorListener?.onErrorResponse(error)
                    },
                    token)

            requestQueue.add(request)
        }
    }
}