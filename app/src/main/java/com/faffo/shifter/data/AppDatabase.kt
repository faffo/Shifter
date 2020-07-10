package com.faffo.shifter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.faffo.shifter.data.entities.*
import com.faffo.shifter.data.entities.TODO.Department

@Database(
    version = 4,
    entities = [Employee::class, Department::class, Shift::class, Holiday::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun holidayDao(): HolidayDao
    abstract fun shiftDao(): ShiftDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance


            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}