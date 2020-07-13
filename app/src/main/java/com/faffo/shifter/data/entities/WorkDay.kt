package com.faffo.shifter.data.entities

import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

data class WorkDay(val date: LocalDate) {
    var calendarDate: Calendar = GregorianCalendar()
    lateinit var employees: List<Employee>
    lateinit var dayType: DayType

    init {
        calendarDate =
            Calendar.Builder().setDate(date.year, date.monthValue, date.dayOfMonth).build()
        when (date.dayOfWeek) {
            DayOfWeek.SATURDAY -> dayType = DayType.SATURDAY
            DayOfWeek.SUNDAY -> dayType = DayType.SUNDAY
        }
    }
}

enum class DayType {
    WEEKDAY,
    SATURDAY,
    SUNDAY,
    HOLIDAY
}