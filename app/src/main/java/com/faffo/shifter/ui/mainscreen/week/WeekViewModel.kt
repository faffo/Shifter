package com.faffo.shifter.ui.mainscreen.week

import android.app.Application
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.AndroidViewModel
import com.faffo.shifter.ui.mainscreen.day.DayFragment

class WeekViewModel(application: Application) : AndroidViewModel(application) {

    fun setDays(calendarDate: Calendar, currentMonth: Int, view: View): MutableList<Pair<Int, DayFragment>> {
        var daysList: MutableList<Pair<Int, DayFragment>> = mutableListOf()
        for (i: Int in 0..6) {
            if (i > 0)
                calendarDate.add(Calendar.DAY_OF_YEAR, 1)
            val bundle = Bundle()
            bundle.putInt("year", calendarDate.get(Calendar.YEAR))
            bundle.putInt("month", calendarDate.get(Calendar.MONTH))
            bundle.putInt("day", calendarDate.get(Calendar.DAY_OF_MONTH))
            bundle.putInt("currentMonth", currentMonth)

            val dayFragment = DayFragment()
            dayFragment.arguments = bundle

            var id: Int = -1
            val fragmentContainerView: FragmentContainerView

            when (i) {
                0 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_monday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
                1 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_tuesday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
                2 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_wednesday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
                3 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_thursday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
                4 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_friday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
                5 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_saturday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
                6 -> {
                    fragmentContainerView =
                        view.findViewWithTag<FragmentContainerView>("fragment_sunday")
                    fragmentContainerView.id = View.generateViewId()
                    id = fragmentContainerView.id //R.id.fragment_monday
                }
            }

            daysList.add(Pair(id, dayFragment))
        }
        return daysList
    }
}