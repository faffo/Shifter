package com.faffo.shifter.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["employee", "day"], unique = true)]
)
data class Shift(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val type: Int,
    val length: Int,
    @ColumnInfo(name = "employee") val employee: String, //@Embedded val employee: Employee,
    @ColumnInfo(name = "day") val day: Int,
    val month: Int,
    val year: Int,
    val color: String?
)