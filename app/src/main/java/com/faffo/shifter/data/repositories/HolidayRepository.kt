package com.faffo.shifter.data.repositories

import androidx.lifecycle.LiveData
import com.faffo.shifter.data.entities.Holiday
import com.faffo.shifter.data.entities.HolidayDao

class HolidayRepository(private val holidayDao: HolidayDao) {
    fun getholidaysDirect(): List<Holiday> = holidayDao.getHolidayDirect()
    suspend fun getHolidays(): List<Holiday> = holidayDao.getHolidayAll()
    fun getHolidaysLD(): LiveData<List<Holiday>> = holidayDao.getHolidayAllLD()
    suspend fun refreshHolidays(holidays: List<Holiday>): List<Long> =
        holidayDao.refreshHoliday(holidays)

    suspend fun getHolidayByDate(date: Long) = holidayDao.getHolidayByDate(date)

    suspend fun insertHoliday(holiday: Holiday) = holidayDao.insertHoliday(holiday)
    suspend fun insertHolidayAll(holidays: List<Holiday>): List<Long> =
        holidayDao.insertHolidayAll(holidays)

    suspend fun getHolydayByMonthDay(month: Int, day: Int): List<Holiday> =
        holidayDao.getHolydayByMonthDay(month, day)
}