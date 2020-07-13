package com.faffo.shifter.ui.splashscreen

import SplashViewModel
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.faffo.shifter.contentresolvers.CalendarContentResolver
import com.faffo.shifter.data.AppDatabase
import com.faffo.shifter.ui.loginscreen.LoginActivity
import com.faffo.shifter.ui.mainscreen.MainActivity
import com.faffo.shifter.utils.Helpers
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
        calendarContentResolver =
            CalendarContentResolver(this)

        var splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        if (!Helpers.checkLogin(this)) {
            startActivity(Intent(this, LoginActivity::class.java).apply { })
        } else {
            startActivity(Intent(this, MainActivity::class.java).apply { })
        }
        finish()
        return
    }
}