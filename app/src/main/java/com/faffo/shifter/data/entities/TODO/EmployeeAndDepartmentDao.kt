package com.faffo.shifter.data.entities.TODO

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EmployeeAndDepartmentDao {

    @Transaction
    @Query("SELECT * FROM Employee")
    fun getEmployeeAndDepartments(): List<EmployeeAndDepartment>
}