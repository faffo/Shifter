package com.faffo.shifter.ui.mainscreen.month

import android.app.Application
import android.icu.util.Calendar
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.faffo.shifter.R
import com.faffo.shifter.ui.mainscreen.week.WeekFragment

class MonthViewModel(application: Application) : AndroidViewModel(application) {
    var firstWeek: Int = -1
    val app = application

    fun setUpView(calendar: Calendar): MutableList<Pair<Int, WeekFragment>> {
        firstWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        var weekList: MutableList<Pair<Int, WeekFragment>> = mutableListOf()
        for (i: Int in firstWeek..firstWeek + 5) {
            val weekCalendar = Calendar.getInstance()
            weekCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            weekCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            weekCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))

            weekCalendar.set(Calendar.WEEK_OF_YEAR, i)
            val weekofMonth = i % firstWeek + 1
            var id: Int = -1

            when (weekofMonth) {
                1 -> id = R.id.fragment_week_1
                2 -> id = R.id.fragment_week_2
                3 -> id = R.id.fragment_week_3
                4 -> id = R.id.fragment_week_4
                5 -> id = R.id.fragment_week_5
                6 -> id = R.id.fragment_week_6
            }

            var bundle = Bundle()
            bundle.putInt("weekYear", i)
            bundle.putInt("currentMonth", calendar.get(Calendar.MONTH))

            createWeekFragmentLive().observeForever(object : Observer<WeekFragment>{
                override fun onChanged(weekFragment: WeekFragment) {
                    var week = Pair(id, weekFragment)
                    week.second.arguments = bundle
                    weekList.add(week)
                }
            })

//            var week = Pair(id, WeekFragment())
//            week.second.arguments = bundle
//            weekList.add(week)

        }
        return weekList
    }

    fun createWeekFragmentLive(): MutableLiveData<WeekFragment>{
        return MutableLiveData(WeekFragment())
    }
}