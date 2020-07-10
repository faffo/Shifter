package com.faffo.shifter

import SplashViewModel
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.faffo.shifter.data.AppDatabase
import com.faffo.shifter.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), CoroutineScope {

    //lateinit var splashViewModel: SplashViewModel

    private val callbackId = 42
    lateinit var settings: SharedPreferences
    lateinit var calendarContentResolver: CalendarContentResolver
    var appDatabase: AppDatabase? = null


    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        calendarContentResolver = CalendarContentResolver(this)

        var splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        if (!Helpers.checkLogin(this)) {
            startActivity(Intent(this, LoginActivity::class.java).apply { })
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //var holidays = splashViewModel.holidaysDb
                }
                else -> {
                    // You can directly ask for the permission.
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR
                        ), callbackId
                    )
                }
            }
            startActivity(Intent(this, ShifterActivity::class.java).apply { })
        }
        finish()
        return
    }
}