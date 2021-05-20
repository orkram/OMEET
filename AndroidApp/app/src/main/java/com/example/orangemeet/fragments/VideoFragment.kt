package com.example.orangemeet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.orangemeet.CustomJitsiFragment
import com.example.orangemeet.R
import com.example.orangemeet.UserInfo
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import timber.log.Timber
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1_VIDEO = "param1"
private const val ARG_PARAM2_VIDEO  = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var widok: CustomJitsiFragment? = null
    lateinit var startFragmentButton : Button
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1_VIDEO)
            param2 = it.getString(ARG_PARAM2_VIDEO)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ////
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        progressBar = view.findViewById(R.id.progressBar)
//        startFragmentButton = view.findViewById(R.id.startFragmentButton)
//        startFragmentButton.setTag("off")
//        startFragmentButton.setOnClickListener {
//            widok = CustomJitsiFragment()
//            widok!!.parentFrag = this
//            if(startFragmentButton.tag == "off"){
//            activity?.supportFragmentManager?.commit {
//                setReorderingAllowed(false)
//                // Replace whatever is in the fragment_container view with this fragment
//                //add<CustomJitsiFragment>(R.id.fragmentLayout)
//                add(R.id.fragmentLayout, widok!!)
//                }
//                startFragmentButton.setTag("on")
//
//            }
//            else if(startFragmentButton.tag == "on"){
//
//                //widok.hangUp()
//               // activity?.supportFragmentManager?.commit {
//                 //   setReorderingAllowed(false)
//                    // Replace whatever is in the fragment_container view with this fragment
//
//                   // replace<EmptyMeetingFragment>(R.id.fragmentLayout)
//               // }
//                startFragmentButton.setTag("off")
//            }
//
//        }
        if (!UserInfo.conferenceName.equals("")) {
            progressBar.visibility = View.VISIBLE
            val jitsiFragment = childFragmentManager.findFragmentById(R.id.jitsiFragment) as CustomJitsiFragment


            val userData = JitsiMeetUserInfo()
            var roomName : String = UserInfo.conferenceId;
            userData.setDisplayName(UserInfo.userName)

            jitsiFragment.jitsiView.join(JitsiMeetConferenceOptions.Builder()
                    .setServerURL(URL("http://130.61.186.61"))
                    .setRoom(roomName)
                    .setSubject(UserInfo.conferenceName)
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setUserInfo(userData)
                    .setFeatureFlag("fullscreen.enabled", false)
                    .setFeatureFlag("add-people.enabled", false)
                    .setFeatureFlag("invite.enabled", false)
                    .setFeatureFlag("toolbox.enabled", true)
                    .setFeatureFlag("chat.enabled", true)
                    .setWelcomePageEnabled(false)
                    .build())
            val g = 3
        } else replaceWithInfo()

        /*if (!UserInfo.conferenceName.equals("")) {
            progressBar.visibility = View.VISIBLE
            widok = CustomJitsiFragment()
            widok!!.parentFrag = this
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(false)
                //Replace whatever is in the fragment_container view with this fragment
                add(R.id.fragmentLayout, widok!!)
            }
        } else replaceWithInfo()*/

        return view
        ////
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_video, container, false)
    }

    fun replaceWithInfo()
    {
        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(false)
            // Replace whatever is in the fragment_container view with this fragment
            replace<EmptyMeetingFragment>(R.id.fragmentLayout)
        }
        progressBar.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        //widok?.hangUp()
        super.onDestroy()
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1_VIDEO, param1)
                    putString(ARG_PARAM2_VIDEO, param2)
                }
            }
    }
}