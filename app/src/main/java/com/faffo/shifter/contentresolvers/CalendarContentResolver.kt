package com.faffo.shifter.contentresolvers

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import com.faffo.shifter.data.entities.CalendarEvent
import com.faffo.shifter.data.entities.Holiday
import kotlin.collections.HashSet

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
        "${CalendarContract.Events.DTSTART} <= ? AND ${CalendarContract.Events.DTEND} >= ?"

    fun getDayEvents(
        startDate: Calendar,
        endDate: Calendar? = null,
        selection: String? = EVENTS_SELECTION
    ): List<CalendarEvent> {
        var calendarEvents = mutableListOf<CalendarEvent>()
        var dtStart = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        var dtEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC"))


        dtStart.set(
            startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH),
            startDate.get(Calendar.DAY_OF_MONTH),
            23,
            59,
            59
        )

        if (endDate == null) {
            dtEnd.set(
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
            )
        } else {
            dtEnd.set(
                endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
            )
        }

        var a = dtStart.timeInMillis.toString()
        var b = dtEnd.timeInMillis.toString()

        val cursor: Cursor = getCursor(
            EVENTS_URI,
            EVENT_PROJECTION,
            selection,
            arrayOf(dtStart.timeInMillis.toString(), dtEnd.timeInMillis.toString()),
            null
        )?: return calendarEvents

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

    fun getCalendars(): Set<String> {
        return queryCalendar(CALENDAR_URI)
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

    fun queryCalendar(
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
        cursor?.close()
        return results
    }

}