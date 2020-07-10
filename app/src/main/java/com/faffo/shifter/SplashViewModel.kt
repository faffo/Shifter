import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.faffo.shifter.CalendarContentResolver
import com.faffo.shifter.data.AppDatabase
import com.faffo.shifter.data.entities.Holiday
import com.faffo.shifter.data.repositories.HolidayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    var nInsert: List<Long> = listOf()

    var settings: SharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(application)

    var firstRun = settings.getBoolean("firstRun", true)
    var calendarContentResolver: CalendarContentResolver = CalendarContentResolver(application)

    private val holidayRepository: HolidayRepository
    var holidaysDb: List<Holiday> = listOf()
    val holidaysDbValue: List<Holiday>? = null

    //var holidaysCalendarAsync: List<Holiday>? = null

    suspend fun getHolidaysDbDirect(): List<Holiday> {
        return holidayRepository.getHolidays()
    }

    init {
        val appDatabase: AppDatabase = AppDatabase.getAppDataBase(application)
        holidayRepository = HolidayRepository(appDatabase.holidayDao())

        viewModelScope.launch {

            if (firstRun)
                refreshHolidays()

            holidaysDb = holidayRepository.getHolidays()
            var holidaysCalendar = calendarContentResolver.getHolidaysAsync()

            if (holidaysDb == null || holidaysDb.count() < 1) {
                val newHolidays = holidaysCalendar
                if (newHolidays != null) {
                    nInsert = holidayRepository.refreshHolidays(newHolidays)
                }
            }
        }

        firstRun = false
    }

    suspend fun isHolidaysPopulated(): Boolean {
        return (holidayRepository.getHolidays().count() > 0)
    }

    suspend fun getHolidays(): List<Holiday> {
        return holidayRepository.getHolidays()
    }

    suspend fun refreshHolidays(): List<Long> {
        var id: Long = 0
        var holidays: MutableList<Holiday> = mutableListOf()

        holidays.add(
            Holiday(
                id = id,
                title = "Capodanno",
                month = 1,
                day = 1
            )
        )

        holidays.add(
            Holiday(
                id = id,
                title = "Epifania",
                month = 1,
                day = 6
            )
        )

        holidays.add(
            Holiday(
                id = id,
                title = "Liberazione",
                month = 4,
                day = 25
            )
        )

        holidays.add(
            Holiday(
                id = id,
                title = "Festa del Lavoro",
                month = 5,
                day = 1
            )
        )
        holidays.add(
            Holiday(
                id = id,
                title = "Festa della Repubblica",
                month = 6,
                day = 2
            )
        )
        holidays.add(
            Holiday(
                id = id,
                title = "Ferragosto",
                month = 8,
                day = 15
            )
        )

        holidays.add(
            Holiday(
                id = id,
                title = "Ognisanti",
                month = 11,
                day = 1
            )
        )
        holidays.add(
            Holiday(
                id = id,
                title = "Immacolata Concezione",
                month = 12,
                day = 8
            )
        )
        holidays.add(
            Holiday(
                id = id,
                title = "Natale",
                month = 12,
                day = 25
            )
        )
        holidays.add(
            Holiday(
                id = id,
                title = "Santo Stefano",
                month = 12,
                day = 26
            )
        )
        var result = holidayRepository.refreshHolidays(holidays)

        return result
    }

    private suspend fun initPrefereces() {
        viewModelScope.launch {
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
    }

    private fun setDefaultPreferences() {
        settings.edit().putString("theme", AppCompatDelegate.MODE_NIGHT_YES.toString())
            .putString("locale", Locale.ENGLISH.toLanguageTag())
            .putBoolean("firstRun", false)
            .apply()
    }
}