package com.faffo.shifter

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    lateinit var settings: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)
        settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this.context)

        var preferenceScreen = preferenceManager.preferenceScreen


        var themePreference =
            preferenceScreen.findPreference<DropDownPreference>(resources.getString(R.string.settingsTheme))
                ?: DropDownPreference(this.context).apply {
                    key = "theme"
                    title = "Theme"
                }
        themePreference.entries = arrayOf("Light", "Dark", "Systen Default")
        themePreference.entryValues = arrayOf(
            AppCompatDelegate.MODE_NIGHT_NO.toString(),
            AppCompatDelegate.MODE_NIGHT_YES.toString(),
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString()
        )
        themePreference.setDefaultValue(
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString("theme", null)
        )
        themePreference.onPreferenceChangeListener = this

        var localePreference =
            preferenceScreen.findPreference<DropDownPreference>(resources.getString(R.string.settingsLocale))
                ?: DropDownPreference(this.context).apply {
                    key = resources.getString(R.string.settingsLocale)
                    title = "Locale"
                }

        localePreference.onPreferenceChangeListener = this


    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference?.key) {
            resources.getString(R.string.settingsTheme) -> {
                settings.edit()
                    .putString(resources.getString(R.string.settingsTheme), newValue.toString())
                    .apply()
                AppCompatDelegate.setDefaultNightMode(Integer.parseInt(newValue.toString()))
                return true
            }
            resources.getString(R.string.settingsLocale) -> {
                settings.edit()
                    .putString(resources.getString(R.string.settingsLocale), newValue.toString())
                    .apply()
                AppCompatDelegate.setDefaultNightMode(Integer.parseInt(newValue.toString()))
                return true
            }
        }
        return true
    }

}