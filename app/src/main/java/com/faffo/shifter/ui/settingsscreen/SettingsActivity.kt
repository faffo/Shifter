package com.faffo.shifter.ui.settingsscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import com.faffo.shifter.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat_DayNight)
        setContentView(R.layout.activity_settings)


        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content,
                SettingsFragment()
            )
            .commit()
    }

}