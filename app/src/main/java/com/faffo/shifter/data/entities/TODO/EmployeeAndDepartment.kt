package com.faffo.shifter.data.entities.TODO

import androidx.room.Embedded
import androidx.room.Relation
import com.faffo.shifter.data.entities.Employee

data class EmployeeAndDepartment(
    @Embedded val department: Department,
    @Relation(
        parentColumn = "login",
        entityColumn = "code"
    )
    val employees: List<Employee>
)