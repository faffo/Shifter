package com.faffo.shifter.data.repositories

import androidx.lifecycle.LiveData
import com.faffo.shifter.data.entities.Shift
import com.faffo.shifter.data.entities.ShiftDao

class ShiftRepository(private val shiftDao: ShiftDao) {
    suspend fun upsertShift(shift: Shift): Long = shiftDao.upsert(shift)
    suspend fun insertShift(shift: Shift): Long = shiftDao.insert(shift)
    suspend fun getShift(year: Int, month: Int, day: Int) = shiftDao.getShift(year, month, day)
    fun getShiftLive(year: Int, month: Int, day: Int): LiveData<List<Shift>> = shiftDao.getShiftLive(year, month, day)
}