package com.faffo.shifter.data.entities

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM Employee")
    suspend fun getEmployeeAll(): List<Employee>

    @Query("SELECT * FROM Employee WHERE login = :account LIMIT 1")
    suspend fun getEmployeeByAccount(account: String): Employee

    @Transaction
    @Query("SELECT * FROM Employee")
    suspend fun getEmployeeAllWithShifts(): List<EmployeeAndShift>
}