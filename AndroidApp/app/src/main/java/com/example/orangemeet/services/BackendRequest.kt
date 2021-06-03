//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.services

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.Charset

abstract class BackendRequest<T>(
        method: Int,
        url: String?,
        requestBody: String?,
        listener: Response.Listener<T>?,
        errorListener: Response.ErrorListener?,
        token : String??
) : JsonRequest<T>(method, url, requestBody, listener, errorListener) {

    private val token : String? = token

    override fun getHeaders(): MutableMap<String, String> {
        val headersMap = HashMap<String, String>()
        if(token != null){
            headersMap["accept"] = "*/*"
            headersMap["authorization"] = "Bearer " + token
        }
        return headersMap
    }

}

open class BackendRequestJsonObject :
        BackendRequest<JSONObject> {

    constructor(method: Int,
                url: String?,
                requestBody: JSONObject?,
                listener: Response.Listener<JSONObject>?,
                errorListener: Response.ErrorListener?,
                token: String?
    ) : super(method, url, requestBody.toString(), listener, errorListener, token)

    constructor(method: Int,
                url: String?,
                requestBody: JSONArray?,
                listener: Response.Listener<JSONObject>?,
                errorListener: Response.ErrorListener?,
                token: String?
    ) : super(method, url, requestBody.toString(), listener, errorListener, token)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        return try {
            val jsonString = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            try{
                Response.success(JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response))
            }catch (ex : JSONException){
                Response.success(JSONObject(), HttpHeaderParser.parseCacheHeaders(response))
            }

        }catch (ex : Exception){
            return Response.error(VolleyError(ex.message))
        }
    }
}

open class BackendRequestJsonArray :
        BackendRequest<JSONArray> {

    constructor(method: Int,
                url: String?,
                requestBody: JSONObject?,
                listener: Response.Listener<JSONArray>?,
                errorListener: Response.ErrorListener?,
                token: String?
    ) : super(method, url, requestBody.toString(), listener, errorListener, token)

    constructor(method: Int,
                url: String?,
                requestBody: JSONArray?,
                listener: Response.Listener<JSONArray>?,
                errorListener: Response.ErrorListener?,
                token: String?
    ) : super(method, url, requestBody.toString(), listener, errorListener, token)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONArray> {
        return try {
            val jsonString = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response))
        }catch (ex : Exception){
            return Response.error(VolleyError(ex.message))
        }
    }
}
