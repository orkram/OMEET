import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orangemeet.userInfo
import org.json.JSONObject

class BackendCommunication {
    companion object{
        val client_id = "orange-app"
        val client_secret = "f0a42c65-a82a-431c-b4e6-36985039a434"
        val backendUrl = "http://130.61.186.61:9000"

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

        fun GetContactsList(context: Context, listener: Response.Listener<JSONObject>?,
                            errorListener: Response.ErrorListener?){

        }

        fun GetMeetingsList(context: Context, listener: Response.Listener<JSONObject>?,
                            errorListener: Response.ErrorListener?){

        }
    }
}