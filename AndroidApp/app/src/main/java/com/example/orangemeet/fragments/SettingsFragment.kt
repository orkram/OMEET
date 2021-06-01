package com.example.orangemeet.fragments

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.android.volley.Response
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.R
import com.example.orangemeet.RemoteSettings

class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var startWithMicPref : SwitchPreferenceCompat
    lateinit var startWithCamPref : SwitchPreferenceCompat
    lateinit var privateUserPref : SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        startWithMicPref = this.findPreference<SwitchPreferenceCompat>("start_with_audio")!!
        startWithCamPref = this.findPreference<SwitchPreferenceCompat>("start_with_video")!!
        privateUserPref = this.findPreference<SwitchPreferenceCompat>("private_user")!!

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
        BackendCommunication.updateSettings(requireContext(),
                newSettings,
                null, null)
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