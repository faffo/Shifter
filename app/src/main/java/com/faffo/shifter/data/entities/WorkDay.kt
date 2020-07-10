package com.faffo.shifter.data.entities

//import com.google.api.client.auth.oauth2.Credential
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.*
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.JsonFactory
//import com.google.api.client.json.jackson2.JacksonFactory
//import com.google.api.client.util.store.FileDataStoreFactory
//import com.google.api.services.calendar.CalendarScopes
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

data class WorkDay(val date: LocalDate) {
    var calendarDate: Calendar = GregorianCalendar();
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