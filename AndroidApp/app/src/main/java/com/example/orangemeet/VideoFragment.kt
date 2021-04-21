package com.example.orangemeet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ////
        val view = inflater!!.inflate(R.layout.fragment_video, container, false)

        val mic_icon = view.findViewById(R.id.mic_icon) as ImageView
        mic_icon.setTag("turned_on")
        mic_icon.setOnClickListener{
            if (mic_icon.getTag().equals("turned_on"))
            {
                mic_icon.setImageResource(R.drawable.ic_mic_off)
                mic_icon.setTag("turned_off")
            }
            else
            {
                mic_icon.setImageResource(R.drawable.ic_mic_on)
                mic_icon.setTag("turned_on")
            }
        }

        val cam_icon = view.findViewById(R.id.cam_icon) as ImageView
        cam_icon.setTag("turned_on")
        cam_icon.setOnClickListener{
            if (cam_icon.getTag().equals("turned_on"))
            {
                cam_icon.setImageResource(R.drawable.ic_videocam_off)
                cam_icon.setTag("turned_off")
            }
            else
            {
                cam_icon.setImageResource(R.drawable.ic_videocam_on)
                cam_icon.setTag("turned_on")
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}