package com.example.orangemeet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.orangemeet.R

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

        val micIcon = view.findViewById(R.id.mic_icon) as ImageView
        micIcon.tag = "turned_on"
        micIcon.setOnClickListener{
            if (micIcon.tag == "turned_on")
            {
                micIcon.setImageResource(R.drawable.ic_mic_off)
                micIcon.tag = "turned_off"
            }
            else
            {
                micIcon.setImageResource(R.drawable.ic_mic_on)
                micIcon.tag = "turned_on"
            }
        }

        val camIcon = view.findViewById(R.id.cam_icon) as ImageView
        camIcon.tag = "turned_on"
        camIcon.setOnClickListener{
            if (camIcon.tag == "turned_on")
            {
                camIcon.setImageResource(R.drawable.ic_videocam_off)
                camIcon.tag = "turned_off"
            }
            else
            {
                camIcon.setImageResource(R.drawable.ic_videocam_on)
                camIcon.tag = "turned_on"
            }
        }
        return view
        ////
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_video, container, false)
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