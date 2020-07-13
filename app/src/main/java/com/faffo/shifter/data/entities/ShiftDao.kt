package com.faffo.shifter.data.entities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShiftDao {
    @Query("SELECT * FROM Shift")
    suspend fun getAll(): List<Shift>

    @Query("SELECT * FROM Shift WHERE year = :year AND month = :month AND day = :day")
    suspend fun getShift(year: Int, month: Int, day: Int): List<Shift>

    @Query("SELECT * FROM Shift WHERE year = :year AND month = :month AND day = :day")
    fun getShiftLive(year: Int, month: Int, day: Int): LiveData<List<Shift>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shift: Shift): Long

    @Update
    suspend fun update(shift: Shift): Int

    suspend fun upsert(shift: Shift): Long{
        val updateReturn = update(shift)
        if(updateReturn < 1)
             return insert(shift)
        else
            return updateReturn.toLong()
    }

}