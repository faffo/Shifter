package com.faffo.shifter.ui.mainscreen.day

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.util.Calendar
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.faffo.shifter.R
import com.faffo.shifter.contentresolvers.CalendarContentResolver
import com.faffo.shifter.data.AppDatabase
import com.faffo.shifter.data.entities.CalendarEvent
import com.faffo.shifter.data.entities.Holiday
import com.faffo.shifter.data.entities.Shift
import com.faffo.shifter.data.repositories.HolidayRepository
import com.faffo.shifter.data.repositories.ShiftRepository
import com.faffo.shifter.utils.ShiftType
import kotlinx.android.synthetic.main.fragment_day.view.*

class DayViewModel(application: Application) : AndroidViewModel(application) {
    val app = application
    var calendarEvents: List<CalendarEvent> = mutableListOf()
    val settings: SharedPreferences

    var calendarContentResolver: CalendarContentResolver =
        CalendarContentResolver(application)

    private val holidayRepository: HolidayRepository
    private val shiftRepository: ShiftRepository

    var holidaysCalendar: List<Holiday>?

    init {
        val appDatabase: AppDatabase = AppDatabase.getAppDataBase(application)
        holidayRepository = HolidayRepository(appDatabase.holidayDao())
        shiftRepository = ShiftRepository(appDatabase.shiftDao())
        holidaysCalendar = calendarContentResolver.getHolidays()

        settings =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(app.applicationContext)

    }

    suspend fun getHolidayByMonthDay(month: Int, day: Int): List<Holiday> {
        return holidayRepository.getHolydayByMonthDay(month, day)
    }

    fun setupView(calendar: Calendar, currentMonth: Int, today: Calendar, view: View) {
        val dayNumber: Int = calendar.get(Calendar.DAY_OF_MONTH)
        view.tvDayNumber.text = dayNumber.toString()


        if (calendar.get(Calendar.MONTH) != currentMonth) {
            view.tvDayNumber.setTextColor(
                AppCompatResources.getColorStateList(
                    view.context,
                    R.color.colorInactive
                )
            )
            view.background =
                AppCompatResources.getDrawable(
                    view.context,
                    R.drawable.day_border_inactive
                )?.let {
                    DrawableCompat.wrap(it)
                }
        }

        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> {
                view.background =
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.holiday_border
                    )?.let {
                        DrawableCompat.wrap(it)
                    }
                view.tvDayNumber.setTextColor(app.resources.getColor(R.color.colorHoliday, null))
            }
        }

        getHoliday(calendar, view)

        if ((calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR))
            && (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
        ) {
            view.background =
                AppCompatResources.getDrawable(
                    view.context,
                    R.drawable.day_border_today
                )?.let {
                    DrawableCompat.wrap(it)
                }
        }

        getShiftsLive(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            view
        )

        var day = calendar.timeInMillis
        var eventBar = view.findViewById<FrameLayout>(R.id.bar_calendar_event)
        var eventTitle = view.findViewById<TextView>(R.id.tvCalendarEventTitle)

        if (settings.getBoolean(app.resources.getString(R.string.showCalendarEvents), false))
            getCalendarEventLive(calendar).observeForever(object : Observer<List<CalendarEvent>>{
                override fun onChanged(calendarEvents: List<CalendarEvent>?) {
                    if (calendarEvents != null && calendarEvents.any()) {
                        eventBar.setBackgroundColor(view.resources.getColor(R.color.googleCalendarEvent, null))
                        eventTitle.text = calendarEvents[0].title
                    } else {
                        eventBar.setBackgroundColor(Color.TRANSPARENT)
                        eventTitle.text = ""
                    }
                }

            })

//        if (settings.getBoolean(app.resources.getString(R.string.showCalendarEvents), false))
//            calendarEvents = getCalendarEvent(calendar)
//        if (calendarEvents.any()) {
//            eventBar.setBackgroundColor(view.resources.getColor(R.color.googleCalendarEvent, null))
//            eventTitle.text = calendarEvents[0].title
//        } else {
//            eventBar.setBackgroundColor(Color.TRANSPARENT)
//            eventTitle.text = ""
//        }

    }

    suspend fun saveShift(shift: Shift) {
        //shiftRepository.insertShift(shift)
        shiftRepository.upsertShift(shift)
    }

    suspend fun getShifts(year: Int, month: Int, day: Int): List<Shift> {
        return shiftRepository.getShift(year, month, day)
    }

    fun setShifts(shifts: List<Shift>?, view: View) {
        var eventBar = view.findViewById<FrameLayout>(R.id.bar_shift)
        if (shifts != null && shifts.any()) {
            when (shifts[0].type) {
                ShiftType.FULL.ordinal -> {
                    eventBar.setBackgroundColor(
                        view.resources.getColor(
                            R.color.fullShift,
                            null
                        )
                    )
                }
                ShiftType.HALF.ordinal -> {
                    eventBar.setBackgroundColor(
                        view.resources.getColor(
                            R.color.halfShift,
                            null
                        )
                    )
                }
            }
        }else
            eventBar.setBackgroundColor(Color.TRANSPARENT)
    }

    fun getShiftsLive(year: Int, month: Int, day: Int, view: View) {
        shiftRepository.getShiftLive(year, month, day)
            .observeForever(object : Observer<List<Shift>> {
                override fun onChanged(t: List<Shift>?) {
                    val shifts = t
                    setShifts(shifts, view)
                }
            })
    }

    fun setHoliday(isHoliday: Boolean, view: View) {
        view.background =
            AppCompatResources.getDrawable(
                view.context,
                R.drawable.holiday_border
            )?.let {
                DrawableCompat.wrap(it)
            }
        if (isHoliday)
            view.tvDayNumber.setTextColor(app.resources.getColor(R.color.colorHoliday, null))
        else
            view.tvDayNumber.setTextColor(Color.TRANSPARENT)
    }

    fun getHoliday(calendar: Calendar, view: View) {
        holidayRepository.getHolidaysByMonthDayLive(
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        ).observeForever(object : Observer<List<Holiday>> {
            override fun onChanged(holidays: List<Holiday>?) {
                if (holidays != null && holidays.any()) {
                    setHoliday(true, view)
                }
            }

        })
    }

    fun getCalendarEvent(
        calendarDateStart: Calendar,
        calendarDateEnd: Calendar? = null
    ): List<CalendarEvent> {
        return calendarContentResolver.getDayEvents(calendarDateStart, calendarDateEnd)
    }

    fun getCalendarEventLive(
        calendarDateStart: Calendar,
        calendarDateEnd: Calendar? = null
    ): LiveData<List<CalendarEvent>> {
        return MutableLiveData(calendarContentResolver.getDayEvents(calendarDateStart, calendarDateEnd))
    }
}