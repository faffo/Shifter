package com.faffo.shifter.ui.mainscreen

import android.app.Application
import android.content.SharedPreferences
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.faffo.shifter.R
import java.time.LocalDate
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val locale: Locale
    val settings: SharedPreferences

    var context = application.applicationContext

    var theme = -1

    init {
        settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        setTheme()
        locale = Locale.forLanguageTag(
            settings.getString(context.resources.getString(R.string.settingsLocale), "en")
                ?: "en"
        )
    }

    fun setTheme(){
        theme = Integer.parseInt(settings.getString("theme", null)
            ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString())
    }

    private fun calendarToLocalDate(calendar: Calendar): LocalDate {
        return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}