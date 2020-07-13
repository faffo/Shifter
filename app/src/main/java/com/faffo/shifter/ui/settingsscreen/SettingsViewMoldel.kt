package com.faffo.shifter.ui.settingsscreen

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel

class SettingsViewMoldel(application: Application) : AndroidViewModel(application) {

    val app: Application = application

    fun checkCalendarPermissions(): Boolean {
        if (
            ContextCompat.checkSelfPermission(
                app.applicationContext.applicationContext, Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(
                app.applicationContext.applicationContext, Manifest.permission.WRITE_CALENDAR
            ) == PackageManager.PERMISSION_DENIED
        ) {

            return false
        }

        return true
    }

}
