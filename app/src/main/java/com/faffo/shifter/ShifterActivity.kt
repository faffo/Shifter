package com.faffo.shifter

import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.faffo.shifter.widgets.MonthFragment
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate
import java.util.*

private const val DEBUG_TAG = "Gestures"


class ShifterActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    //val today: LocalDate = LocalDate.now()
    val calendarDate: Calendar = Calendar.getInstance();
    var currentFragmentId: Int = -1

    lateinit var settings: SharedPreferences
    lateinit var locale: Locale
    private lateinit var mDetector: GestureDetectorCompat

    lateinit var toolbar: Toolbar
    lateinit var toggleNavigation: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration
    //var defaultTheme = AppCompatDelegate.MODE_NIGHT_YES

    override fun onCreate(savedInstanceState: Bundle?) {
        settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        //settings_keys.edit().putBoolean("isLoggedIn", false).apply();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_shifter)
        initPrefereces()

        toolbar = findViewById(R.id.shifter_toolbar)

        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.shifter_drawer_layout)
        navView = findViewById(R.id.shifter_navigation_view)
        //navController = findNavController(R.id.shifter_navigation_host)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_month, R.id.nav_settings
            ), drawerLayout
        )

        navView.setNavigationItemSelectedListener(this)

        toggleNavigation = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggleNavigation)
        toggleNavigation.isDrawerSlideAnimationEnabled = true
        toggleNavigation.syncState()

        supportFragmentManager.beginTransaction().add(R.id.fragment_drawer_content, MonthFragment())
            .commit()

        var calendarContentResolver = CalendarContentResolver(this)

        //Check National Holidays
        var holidays = calendarContentResolver.getHolidays()


//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_CALENDAR
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        var cur = this.contentResolver.query(
//            CalendarContract.Events.CONTENT_URI,
//            arrayOf(
//                CalendarContract.Events.OWNER_ACCOUNT,
//                CalendarContract.Events.CALENDAR_ID,
//                CalendarContract.Events.CALENDAR_DISPLAY_NAME,
//                CalendarContract.Events.TITLE,
//                CalendarContract.Events.DESCRIPTION,
//                CalendarContract.Events.DTSTART,
//                CalendarContract.Events.DTEND,
//                CalendarContract.Events.ALL_DAY,
//                CalendarContract.Events.EVENT_LOCATION
//            ),
//            "${CalendarContract.Events.OWNER_ACCOUNT} = ?",
//            arrayOf("faffo665@gmail.com"),
//            null
//        )
//        if (cur != null) {
//            while (cur.moveToNext()){
//                var gne = CalendarEvent(cur)
//                var gni = 1
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var gne = item.itemId
        when (item.itemId) {
            R.id.nav_month -> {

            }
            R.id.nav_settings -> {
                openSettings(item)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun calendarToLocalDate(calendar: Calendar): LocalDate {
        return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun initPrefereces() {
        locale = Locale.forLanguageTag(
            settings.getString(resources.getString(R.string.settingsLocale), "en")
                ?: "en"
        )

        AppCompatDelegate.setDefaultNightMode(
            Integer.parseInt(
                settings.getString("theme", null) ?: AppCompatDelegate.MODE_NIGHT_YES.toString()
            )
        )

        var theme = settings.getString("theme", null)
        if (theme == null) {
            theme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString()
            settings.edit().putString("theme", theme).apply()
        }
    }

    fun changeTheme(item: MenuItem) {

//        when (AppCompatDelegate.getDefaultNightMode()) {
//            AppCompatDelegate.MODE_NIGHT_NO -> defaultTheme = AppCompatDelegate.MODE_NIGHT_YES // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            AppCompatDelegate.MODE_NIGHT_YES -> defaultTheme =
//                AppCompatDelegate.MODE_NIGHT_NO // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> defaultTheme =
//                AppCompatDelegate.MODE_NIGHT_YES // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            AppCompatDelegate.MODE_NIGHT_AUTO_TIME -> defaultTheme =
//                AppCompatDelegate.MODE_NIGHT_YES // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> defaultTheme =
//                AppCompatDelegate.MODE_NIGHT_YES // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED -> defaultTheme =
//                AppCompatDelegate.MODE_NIGHT_YES // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
//        settings_keys.edit().putInt("theme", AppCompatDelegate.getDefaultNightMode()).apply()
    }

    fun openSettings(item: MenuItem) {
        startActivity(
            Intent(this, SettingsActivity::class.java).apply { }
        )
    }


}