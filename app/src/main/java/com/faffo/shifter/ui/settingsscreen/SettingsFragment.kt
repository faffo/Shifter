package com.faffo.shifter.ui.settingsscreen

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import com.faffo.shifter.R

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    lateinit var settings: SharedPreferences
    lateinit var settingsViewMoldel: SettingsViewMoldel
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    var calendarPermission: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = this.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            calendarPermission = (result.getOrDefault(Manifest.permission.READ_CALENDAR, false)
                    && result.getOrDefault(Manifest.permission.WRITE_CALENDAR, false))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        settingsViewMoldel = ViewModelProvider(this).get(SettingsViewMoldel::class.java)
        return view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)
        settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this.context)

        var preferenceScreen = preferenceManager.preferenceScreen

        var themePreference =
            preferenceScreen.findPreference<DropDownPreference>(
                resources.getString(
                    R.string.settingsTheme
                )
            )
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

        //Set defaul setting lister for all preferences to this class.
        setPreferenceListener(preferenceScreen, listener = this)


    }

    private fun setPreferenceListener(
        preference: Preference,
        listener: Preference.OnPreferenceChangeListener
    ) {
        when (preference) {
            is PreferenceScreen -> for (i: Int in 0 until preference.preferenceCount)
                setPreferenceListener(preference[i], listener)
            is PreferenceCategory -> for (i: Int in 0 until preference.preferenceCount)
                setPreferenceListener(preference[i], listener)
            else -> preference.onPreferenceChangeListener = listener
        }
    }

    fun askCalendarPermissions(): Boolean {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            )
        )
        return calendarPermission
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
                return true
            }
            resources.getString(R.string.showCalendarEvents) -> {
                settings.edit()
                    .putBoolean(
                        resources.getString(R.string.showCalendarEvents),
                        newValue as Boolean
                    )
                    .apply()
                if (newValue) {
                    var isPermitted = settingsViewMoldel.checkCalendarPermissions()
                    if (isPermitted) {
                        calendarPermission = true
                        return true
                    } else
                        return askCalendarPermissions()
                }


                return true
            }
        }
        return true
    }

}