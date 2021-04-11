package com.example.orangemeet

import android.os.Bundle
import android.text.BoringLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import java.util.prefs.PreferenceChangeListener

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
        if(mode == true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}