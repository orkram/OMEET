import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class BackendRequest(
    method: Int,
    url: String?,
    listener: Response.Listener<String>?,
    errorListener: Response.ErrorListener?,
    token : String?
) : StringRequest(method, url, listener, errorListener) {

    private val token : String?
    init {
        this.token = token
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headersMap = HashMap<String, String>()
        if(token != null){
            headersMap.put("accept", "*/*")
            headersMap.put("authorization", "Bearer " + token)
        }
        return headersMap
    }

}