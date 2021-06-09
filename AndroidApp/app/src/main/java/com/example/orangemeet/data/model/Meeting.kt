//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.data.model

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.orangemeet.R
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Meeting(
        @SerializedName("idMeeting")
        val id: Long,
        val name: String,
        val date: Date,
        val owner: User,
        val roomUrl: String) {

    companion object{

        fun createFromJson(jsonObject: JSONObject) : Meeting {
            return Meeting(
                    jsonObject.getString("idMeeting").toLong(),
                    jsonObject.getString("name"),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("date")),
                    User.fromJson(jsonObject.getJSONObject("owner")),
                    jsonObject.getString("roomUrl"))
        }
    }
}