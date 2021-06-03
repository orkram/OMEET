package com.example.orangemeet.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.orangemeet.R
import com.example.orangemeet.data.model.RemoteSettings
import org.json.JSONObject

class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var settingsViewModel : SettingsViewModel

    lateinit var startWithMicPref : SwitchPreferenceCompat
    lateinit var startWithCamPref : SwitchPreferenceCompat
    lateinit var privateUserPref : SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        startWithMicPref = this.findPreference<SwitchPreferenceCompat>("start_with_audio")!!
        startWithCamPref = this.findPreference<SwitchPreferenceCompat>("start_with_video")!!
        privateUserPref = this.findPreference<SwitchPreferenceCompat>("private_user")!!

        settingsViewModel.updateSettingsResult.observe(this,
                Observer {result ->
                    if(result.error != null){
                        Toast.makeText(requireContext(), result.error!!, Toast.LENGTH_LONG).show()
                    }
                })

        startWithMicPref.setOnPreferenceChangeListener { preference, newValue ->
            updateRemoteSettings(RemoteSettings(newValue as Boolean, startWithCamPref.isChecked, privateUserPref.isChecked))
            true
        }

        startWithCamPref.setOnPreferenceChangeListener { preference, newValue ->
            updateRemoteSettings(RemoteSettings(startWithMicPref.isChecked, newValue as Boolean, privateUserPref.isChecked))
            true
        }

        privateUserPref.setOnPreferenceChangeListener { preference, newValue ->
            updateRemoteSettings(RemoteSettings(startWithMicPref.isChecked, startWithCamPref.isChecked, newValue as Boolean))
            true
        }

        findPreference<SwitchPreferenceCompat>("day_night")?.setOnPreferenceChangeListener { preference, newValue ->
            setDayNight(newValue as Boolean)
            true
        }
    }

    private fun updateRemoteSettings(newSettings: RemoteSettings){
        val settingsJson = JSONObject()
                .put("isDefaultCamOn", newSettings.startWithCam)
                .put("isDefaultMicOn", newSettings.startWithMic)
                .put("isPrivate", newSettings.privateUser)
        settingsViewModel.updateSettings(settingsJson)
        /*BackendCommunication.updateSettings(BackendRequestQueue.getInstance(requireContext()).requestQueue,
                newSettings,
                null, null)*/
    }

    private fun setDayNight(mode : Boolean){
        if(mode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}