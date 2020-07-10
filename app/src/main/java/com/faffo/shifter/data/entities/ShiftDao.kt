package com.faffo.shifter.data.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShiftDao {
    @Query("SELECT * FROM Shift")
    suspend fun getAll(): List<Shift>

    @Query("SELECT * FROM Shift WHERE year = :year AND month = :month AND day = :day")
    suspend fun getShift(year: Int, month: Int, day: Int): List<Shift>

    @Insert
    suspend fun insert(vararg shift: Shift)


}