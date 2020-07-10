package com.faffo.shifter.widgets

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.faffo.shifter.R
import kotlinx.android.synthetic.main.fragment_week.view.*

class WeekFragment() : Fragment() {
    val calendar: Calendar = Calendar.getInstance()

    var currentMonth: Int = -1

    var firstDay: Int = -1
    var lastDay: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentMonth = requireArguments().getInt("currentMonth")

        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.WEEK_OF_YEAR, requireArguments().getInt("weekYear"))
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
//        var year = calendar.get(Calendar.YEAR)
//        var month = calendar.get(Calendar.MONTH) + 1
//        var weekyear = calendar.get(Calendar.WEEK_OF_YEAR)
//        var weekmonth = calendar.get(Calendar.WEEK_OF_MONTH)
//        var day = calendar.get(Calendar.DAY_OF_MONTH)
//

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)

        val weekNumber: Int = calendar.get(Calendar.WEEK_OF_YEAR)

        view.tvWeekN.text = "Week $weekNumber"
        val calendarDate = calendar

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

            childFragmentManager.beginTransaction().add(id, dayFragment).commit()

        }

        return view
    }
}