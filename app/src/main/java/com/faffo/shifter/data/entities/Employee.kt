package com.faffo.shifter.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faffo.shifter.utils.EmploymentType

@Entity
data class Employee(
    @PrimaryKey val login: String,
    val name: String,
    val surname: String,
    val department: String
) {
    var employmentType: Int = EmploymentType.FULL_TIME.hours
}