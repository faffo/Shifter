package com.faffo.shifter.ui.mainscreen

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.faffo.shifter.R
import com.faffo.shifter.ui.mainscreen.day.DayFragment
import com.faffo.shifter.ui.mainscreen.month.MonthFragment
import com.faffo.shifter.ui.mainscreen.week.WeekFragment
import com.faffo.shifter.ui.settingsscreen.SettingsActivity
import com.google.android.material.navigation.NavigationView
import java.time.Month

private const val DEBUG_TAG = "Gestures"


class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var mainViewModel: MainViewModel
    val calendarDate: Calendar = Calendar.getInstance()
    var currentFragmentId: Int = -1

    private lateinit var mDetector: GestureDetectorCompat

    lateinit var toolbar: Toolbar
    lateinit var toggleNavigation: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        //settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        AppCompatDelegate.setDefaultNightMode(mainViewModel.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toolbar = findViewById(R.id.shifter_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        navView = findViewById<NavigationView>(R.id.shifter_navigation_view)
        navView.setNavigationItemSelectedListener(this)

        toggleNavigation = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggleNavigation)
        toggleNavigation.isDrawerIndicatorEnabled = true
        toggleNavigation.syncState()

        createMonthFragmentLive().observe(this,
        object : Observer<MonthFragment>{
            override fun onChanged(t: MonthFragment?) {
                supportFragmentManager.beginTransaction().add(
                    R.id.drawer_content,
                    MonthFragment()
                ).commit()
            }

        })

//        supportFragmentManager.beginTransaction().add(
//            R.id.drawer_content,
//            MonthFragment()
//        ).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var gne = item.itemId
        when (item.itemId) {
            R.id.nav_month -> {
                supportFragmentManager.beginTransaction().replace(R.id.drawer_content, MonthFragment()).commit()
                drawerLayout.closeDrawers()
            }
            R.id.nav_week -> {
                supportFragmentManager.beginTransaction().replace(R.id.drawer_content, WeekFragment()).commit()
                drawerLayout.closeDrawers()
            }
            R.id.nav_day -> {
                supportFragmentManager.beginTransaction().replace(R.id.drawer_content, DayFragment()).commit()
                drawerLayout.closeDrawers()
            }
            R.id.nav_settings -> {
                openSettings(item)
            }
        }
        return true
    }

    fun replaceContent(id: Int, fragment: Fragment) {
        when (id) {
            R.id.nav_month -> {
                supportFragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit()
                drawerLayout.closeDrawers()
            }
            R.id.nav_week -> {
                supportFragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit()
                drawerLayout.closeDrawers()
            }
            R.id.nav_day -> {
                val dayFragment = fragment as DayFragment
                val newDayFragment = DayFragment()

                val bundle = Bundle()
                bundle.putInt("year", dayFragment.year)
                bundle.putInt("month", dayFragment.month)
                bundle.putInt("day", dayFragment.day)
                bundle.putInt("currentMonth", dayFragment.currentMonth)

                newDayFragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.drawer_content, newDayFragment).commit()
                drawerLayout.closeDrawers()
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    fun openSettings(item: MenuItem) {
        startActivity(
            Intent(this, SettingsActivity::class.java).apply { }
        )
    }

    fun createMonthFragmentLive(): MutableLiveData<MonthFragment>{
        return MutableLiveData(MonthFragment())
    }

}