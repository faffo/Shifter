package com.faffo.shifter

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import com.faffo.shifter.data.entities.CalendarEvent
import com.faffo.shifter.data.entities.Holiday

private const val PROJECTION_ID_INDEX: Int = 0
private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
private const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
private const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

class CalendarContentResolver(val ctx: Context) {
    private val _contentResolver: ContentResolver = ctx.contentResolver

    private val ITALIAN_HOLIDAYS_ACCOUNT = "en.italian#holiday@group.v.calendar.google.com"
    private val USA_HOLIDAYS_ACCOUNT = "en.usa#holiday@group.v.calendar.google.com"


    //URIs
    private val CALENDAR_URI: Uri = CalendarContract.Calendars.CONTENT_URI
    private val EVENTS_URI: Uri = CalendarContract.Events.CONTENT_URI

    //Default Projection Fields
    private val CALENDAR_PROJECTION = arrayOf(
        CalendarContract.Calendars.NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        CalendarContract.Calendars.CALENDAR_COLOR,
        CalendarContract.Calendars.VISIBLE,
        CalendarContract.Calendars._ID
    )

    private val EVENT_PROJECTION = arrayOf(
        CalendarContract.Events.OWNER_ACCOUNT,
        CalendarContract.Events.CALENDAR_ID,
        CalendarContract.Events.CALENDAR_DISPLAY_NAME,
        CalendarContract.Events._ID,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.DESCRIPTION,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND,
        CalendarContract.Events.ALL_DAY,
        CalendarContract.Events.EVENT_LOCATION
    )

    private val HOLIDAY_PROJECTION = arrayOf(
        CalendarContract.Events.OWNER_ACCOUNT,
        CalendarContract.Events._ID,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND
    )

    //Default Selection Fields
    private val HOLIDAY_SELECTION =
        "${CalendarContract.Events.OWNER_ACCOUNT} = ?"

    //Default Selection Args
    private val HOLIDAY_SELECTION_ARGS: Array<String> =
        arrayOf("en.italian#holiday@group.v.calendar.google.com")


    private val EVENTS_SELECTION =
        "${CalendarContract.Events.ACCOUNT_NAME} = ?"


    fun getEvents(
        accountName: String,
        selection: String? = EVENTS_SELECTION
    ): List<CalendarEvent>? {
        var calendarEvents = mutableListOf<CalendarEvent>()
        val cursor: Cursor = getCursor(
            EVENTS_URI,
            EVENT_PROJECTION,
            selection,
            arrayOf(accountName),
            null
        )
            ?: return null
        while (cursor.moveToNext()) {
            calendarEvents.add(CalendarEvent(cursor))
        }
        return calendarEvents
    }

    fun getCursor(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): Cursor? {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        return _contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }

//    var calendars = HashSet<String>()
//    var events = HashSet<String>()

    fun getCalendars(): Set<String> {
        return QueryCalendar(CALENDAR_URI)
    }

    fun getHolidays(accountName: String = ITALIAN_HOLIDAYS_ACCOUNT): List<Holiday>? {
        var calendarEvents = mutableListOf<Holiday>()
        val cursor: Cursor = getCursor(
            EVENTS_URI,
            HOLIDAY_PROJECTION,
            HOLIDAY_SELECTION,
            arrayOf(accountName),
            null
        )
            ?: return null
//        while (cursor.moveToNext()) {
//            calendarEvents.add(holidayFactory(cursor))
//        }
        return calendarEvents
    }


    suspend fun getHolidaysAsync(accountName: String = ITALIAN_HOLIDAYS_ACCOUNT): List<Holiday>? {
        var calendarEvents = mutableListOf<Holiday>()
        val cursor: Cursor = getCursor(
            EVENTS_URI,
            HOLIDAY_PROJECTION,
            HOLIDAY_SELECTION,
            arrayOf(accountName),
            null
        )
            ?: return null
//        while (cursor.moveToNext()) {
//            calendarEvents.add(holidayFactory(cursor))
//        }
        return calendarEvents
    }

    fun QueryCalendar(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): HashSet<String> {
        val results: HashSet<String> = HashSet<String>()
        val cursor: Cursor? = _contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val columns: MutableMap<String, String?> = mutableMapOf()
                columns.put(
                    cursor.getColumnName(PROJECTION_ID_INDEX), cursor.getString(
                        PROJECTION_ID_INDEX
                    )
                )
                columns.put(
                    cursor.getColumnName(PROJECTION_ACCOUNT_NAME_INDEX), cursor.getString(
                        PROJECTION_ACCOUNT_NAME_INDEX
                    )
                )
                columns.put(
                    cursor.getColumnName(PROJECTION_DISPLAY_NAME_INDEX), cursor.getString(
                        PROJECTION_DISPLAY_NAME_INDEX
                    )
                )
                columns.put(
                    cursor.getColumnName(PROJECTION_OWNER_ACCOUNT_INDEX), cursor.getString(
                        PROJECTION_OWNER_ACCOUNT_INDEX
                    )
                )
            }
        }
        cursor?.close();
        return results
    }

}