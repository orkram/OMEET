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
import com.example.orangemeet.activities.MainActivity
import com.example.orangemeet.fragments.VideoFragment
import com.facebook.react.ReactInstanceManager
import org.jitsi.meet.sdk.*

import timber.log.Timber
import java.net.URL

class CustomJitsiFragment : JitsiMeetFragment() {

    private var broadcastReceiver : BroadcastReceiver? = null
    var parentFrag : VideoFragment? = null

    var scheduleHide = false

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

        return view
    }

    override fun onDestroyView() {
        Timber.i("OnDestroyView")
        jitsiView.leave()
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.i("OnDestroy")
        jitsiView.leave()
        super.onDestroy()
    }

    override fun onPause() {
        Timber.i("OnPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.i("OnStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.i("OnSaveInstanceState")
        super.onSaveInstanceState(outState)
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
                    UserInfo.conferenceId = ""
                    if(activity != null)
                        (activity as MainActivity).customJitsiFragmentView.visibility = View.GONE
                    else
                        scheduleHide = true
                    UserInfo.conferenceName = ""
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(scheduleHide){
            scheduleHide = false
            (activity as MainActivity).customJitsiFragmentView.visibility = View.GONE
        }
    }


    companion object{

        var savedView : View? = null
    }

    fun hangUp() {
//        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
//        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
        jitsiView.leave()
    }
}