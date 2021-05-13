package com.example.orangemeet

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.orangemeet.fragments.VideoFragment
import org.jitsi.meet.sdk.*

import timber.log.Timber
import java.net.URL

class CustomJitsiFragment : JitsiMeetFragment() {

    private var broadcastReceiver : BroadcastReceiver? = null
    var parentFrag : VideoFragment? = null
    var roomName : String = UserInfo.conferenceName
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        broadcastReceiver =  object : BroadcastReceiver(requireContext()) {
            override fun onReceive(context: Context?, intent: Intent?) {
                onBroadcastReceived(intent)
            }
        }

        registerForBroadcastMessages()

        val userData = JitsiMeetUserInfo()
        userData.setDisplayName(UserInfo.userName)
        userData.setEmail(UserInfo.userEmail)

        jitsiView.join(JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("http://130.61.186.61"))
            .setRoom(roomName)
            .setAudioMuted(false)
            .setVideoMuted(false)
            .setUserInfo(userData)
            .setFeatureFlag("add-people.enabled", false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("toolbox.enabled", true)
            .setFeatureFlag("chat.enabled", true)
            .setWelcomePageEnabled(false)
            .build()
        )
        return view
    }

    override fun onDestroyView() {
        jitsiView.leave()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver!!)
        super.onDestroyView()
    }


    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver!!, intentFilter)
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.getType()) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> {
                    Timber.i("XD Conference Joined with url%s", event.getData().get("url"))
                    UserInfo.isInConference = true
                }
                BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                    Timber.i("XD Conference terminated%s", event.getData().get("url"))
                    UserInfo.isInConference = false
                    parentFrag?.replaceWithInfo()
                }
            }
        }
    }

    fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
    }
}