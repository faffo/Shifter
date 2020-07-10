package com.faffo.shifter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.faffo.shifter.data.AppDatabase
import com.faffo.shifter.data.entities.Holiday
import com.faffo.shifter.data.entities.Shift
import com.faffo.shifter.data.repositories.HolidayRepository
import com.faffo.shifter.data.repositories.ShiftRepository

class DayViewModel(application: Application) : AndroidViewModel(application) {

    var calendarContentResolver: CalendarContentResolver = CalendarContentResolver(application)

    private val holidayRepository: HolidayRepository
    private val shiftRepository: ShiftRepository
    //var holidaysDb: LiveData<List<Holiday>>

    var holidaysCalendar: List<Holiday>?

    init {
        val appDatabase: AppDatabase = AppDatabase.getAppDataBase(application)
        holidayRepository = HolidayRepository(appDatabase.holidayDao())
        shiftRepository = ShiftRepository(appDatabase.shiftDao())
        //holidaysDb = holidayRepository.getHolidays()
        holidaysCalendar = calendarContentResolver.getHolidays()
    }

    suspend fun getHolidayByMonthDay(month: Int, day: Int): List<Holiday> {
        return holidayRepository.getHolydayByMonthDay(month, day)
    }

//    suspend fun getHolidays(): List<Holiday>{
//        return holidayRepository.getHolidays()
//    }
//
//    suspend fun getHolidaysByDate(date: Long): List<Holiday> {
//        return holidayRepository.getHolidayByDate(date)
//    }

    suspend fun saveShift(shift: Shift) {
        shiftRepository.insertShift(shift)
    }

    suspend fun getShifts(year: Int, month: Int, day: Int): List<Shift> {
        return shiftRepository.getShift(year, month, day)
    }
}