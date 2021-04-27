import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.orangemeet.Contact
import com.example.orangemeet.userInfo
import org.json.JSONObject

class BackendCommunication {
    companion object{
        val client_id = "orange-app"
        val client_secret = "f0a42c65-a82a-431c-b4e6-36985039a434"
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
                .put("client_id", "orange-app")
                .put("client_secret", "f0a42c65-a82a-431c-b4e6-36985039a434")
                .put("username", username)
                .put("password", password)

            val loginRequest = JsonObjectRequest(
                Request.Method.POST, loginUrl, loginJson,
                Response.Listener {response ->
                    token = response.getString("access_token")
                    Log.i("BackendCommunication", "Token: " + token)
                    this.username = username
                    listener?.onResponse(response)
                },
                errorListener)

            requestQueue.add(loginRequest)
            userInfo.userName = username;
        }

        fun Register(context : Context, email : String, firstName : String, lastName : String,
                     imgUrl : String, username : String, password : String,
                     listener: Response.Listener<JSONObject>?,
                     errorListener: Response.ErrorListener?){
        }

        fun GetContactsList(context: Context, listener: Response.Listener<List<Contact>>?,
                            errorListener: Response.ErrorListener?){
            val requestQueue = Volley.newRequestQueue(context)

            val request = BackendRequestJsonArray(Request.Method.GET, backendUrl + "/api/v1/contacts/" + username,
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

            val request = BackendRequestJsonObject(Request.Method.DELETE, backendUrl + "/api/v1/contacts/" + username + "?friend=" + friend,
                    JSONObject(),
                    Response.Listener {
                        Log.e("BackendCommunication", "DeleteContact success")
                        listener!!.onResponse(it)
                    },
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "DeleteContact failed: " + it.message)
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
                        Log.e("BackendCommunication", "AddContact success")
                        listener!!.onResponse(it)
                    } ,
                    Response.ErrorListener {
                        Log.e("BackendCommunication", "AddContact failed: " + it.message)
                        errorListener!!.onErrorResponse(it)
                    },
                    token)

            requestQueue.add(request)
        }

        fun GetMeetingsList(context: Context, listener: Response.Listener<JSONObject>?,
                            errorListener: Response.ErrorListener?){

        }
    }
}