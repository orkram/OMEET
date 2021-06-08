package com.example.orangemeet.ui.main.calling

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.orangemeet.R
import com.example.orangemeet.UserInfo
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.data.Result
import com.example.orangemeet.data.model.Meeting
import com.example.orangemeet.ui.utils.ResultInfo
import com.example.orangemeet.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import java.net.URL

class VideoViewModel : ViewModel() {

    fun getColorScheme(fragment: Fragment): Bundle {
        val colorScheme = Bundle()

        val dialogColorScheme = Bundle()
        dialogColorScheme.putString("buttonBackground", Util.rgbString(fragment.resources.getColor(R.color.orange, fragment.context?.theme)))
        colorScheme.putBundle("Dialog", dialogColorScheme)

        val headerColorScheme = Bundle()
        headerColorScheme.putString("background", Util.rgbString(fragment.resources.getColor(R.color.orange, fragment.context?.theme)))
        headerColorScheme.putString("statusBar", "null")
        colorScheme.putBundle("Header", headerColorScheme)

        val chatColorScheme = Bundle()
        chatColorScheme.putString("localMsgBackground","rgb(241, 110, 0)")
        chatColorScheme.putString("remoteMsgBackground","rgb(239, 158, 88)")
        colorScheme.putBundle("Chat", chatColorScheme)
        return colorScheme
    }

    fun startWithAudio(context : Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean("start_with_audio", false)
    }

    fun startWithVideo(context : Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean("start_with_video", false)
    }

    fun getJitsiOptions(fragment: Fragment): JitsiMeetConferenceOptions {
        val context = fragment.requireContext()
        val userData = JitsiMeetUserInfo()
        userData.setDisplayName(UserInfo.userName)

        return JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("http://130.61.186.61"))
                .setRoom(UserInfo.conferenceId)
                .setSubject(UserInfo.conferenceName)
                .setAudioMuted(!startWithAudio(context))
                .setVideoMuted(!startWithVideo(context))
                .setUserInfo(userData)
                .setFeatureFlag("fullscreen.enabled", false)
                .setFeatureFlag("add-people.enabled", false)
                .setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("toolbox.enabled", true)
                .setFeatureFlag("chat.enabled", true)
                .setFeatureFlag("pip.enabled", false)
                .setFeatureFlag("video-share.enabled", false)
                .setFeatureFlag("recording.enabled", false)
                .setFeatureFlag("live-streaming.enabled", false)
                .setWelcomePageEnabled(false)
                .setColorScheme(getColorScheme(fragment))
                .build()
    }
}