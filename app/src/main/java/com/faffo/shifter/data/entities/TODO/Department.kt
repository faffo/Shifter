package com.faffo.shifter.data.entities.TODO

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Department(
    val name: String,
    @PrimaryKey val code: String, val CdC: Int
) {
    var workersPerShift: Int = 2
}