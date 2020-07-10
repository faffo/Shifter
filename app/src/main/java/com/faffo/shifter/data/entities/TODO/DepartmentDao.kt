package com.faffo.shifter.data.entities.TODO

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM DEPARTMENT")
    fun getAll(): List<Department>
}