package com.faffo.shifter.data.entities

import android.database.Cursor
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Holiday")
data class Holiday(
    var account: String = "root",
    @PrimaryKey(autoGenerate = true) var id: Long,
    var title: String,
    var dateStart: String = "",
    var dateEnd: String = "",
    var month: Int,
    var day: Int
)

fun holidayFactory(cursor: Cursor, month: Int, day: Int): Holiday {
    var account = cursor.getString(0)
    var id = cursor.getLong(1)
    var title = cursor.getString(2)
    var dateStart = cursor.getString(3)
    var dateEnd = cursor.getString(4)

    return (Holiday(account, id, title, dateStart, dateEnd, month, day))
}

