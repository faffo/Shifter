package com.faffo.shifter.data.entities

import android.database.Cursor

data class CalendarEvent(val cursor: Cursor) {
    var account = cursor.getString(0)
    var calendar_id = cursor.getLong(1)
    var calendarName = cursor.getString(2)
    var id = cursor.getLong(3)
    var title = cursor.getString(4)
    var description = cursor.getString(5)
    var dateStartLong = cursor.getLong(6)
    var dateEndLong = cursor.getLong(7)
    var allDay = cursor.getInt(8)
    var eventLocation = cursor.getString(9)
}