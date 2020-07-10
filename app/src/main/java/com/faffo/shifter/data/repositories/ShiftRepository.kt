package com.faffo.shifter.data.repositories

import com.faffo.shifter.data.entities.Shift
import com.faffo.shifter.data.entities.ShiftDao

class ShiftRepository(private val shiftDao: ShiftDao) {
    suspend fun insertShift(shift: Shift) = shiftDao.insert(shift)
    suspend fun getShift(year: Int, month: Int, day: Int) = shiftDao.getShift(year, month, day)
}