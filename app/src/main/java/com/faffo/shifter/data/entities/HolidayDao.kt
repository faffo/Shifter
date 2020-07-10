package com.faffo.shifter.data.entities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HolidayDao {
    @Query("SELECT * FROM Holiday")
    fun getHolidayAllLD(): LiveData<List<Holiday>>

    @Query("SELECT * FROM Holiday")
    fun getHolidayDirect(): List<Holiday>

    @Query("SELECT * FROM Holiday")
    suspend fun getHolidayAll(): List<Holiday>

    @Query("SELECT * FROM HOLIDAY WHERE month = :month AND day = :day")
    suspend fun getHolydayByMonthDay(month: Int, day: Int): List<Holiday>

    @Query("SELECT * FROM Holiday WHERE dateStart < :date AND dateEnd > :date")
    suspend fun getHolidayByDate(date: Long): List<Holiday>

    @Query("DELETE FROM Holiday")
    suspend fun deleteHolidayAll()

    @Delete
    suspend fun deleteHoliday(holiday: Holiday)

    @Insert
    suspend fun insertHoliday(vararg holiday: Holiday)

    @Insert
    suspend fun insertHolidayAll(holidays: List<Holiday>): List<Long>

    @Transaction
    suspend fun insertHolidayAllOld(holidays: List<Holiday>) {
        for (holiday in holidays) {
            insertHoliday(holiday)
        }
    }

    @Transaction
    suspend fun refreshHoliday(holidays: List<Holiday>): List<Long> {
        deleteHolidayAll()
        return insertHolidayAll(holidays)
    }

}