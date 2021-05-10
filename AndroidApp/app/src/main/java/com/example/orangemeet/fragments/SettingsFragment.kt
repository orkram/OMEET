package com.example.orangemeet.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.orangemeet.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val dayNightPref = findPreference<SwitchPreferenceCompat>("day_night")

        dayNightPref?.setOnPreferenceChangeListener { preference, newValue ->
            setDayNight(newValue as Boolean)
            true
        }
    }

    private fun setDayNight(mode : Boolean){
        if(mode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}