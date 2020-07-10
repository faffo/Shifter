package com.faffo.shifter.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EmployeeAndShift(
    @Embedded val employee: Employee,
    @Relation(
        parentColumn = "login",
        entityColumn = "employee"
    )
    val shifts: List<Shift>
)