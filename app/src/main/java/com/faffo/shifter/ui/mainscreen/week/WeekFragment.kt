package com.faffo.shifter.ui.mainscreen.week

import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.faffo.shifter.R
import com.faffo.shifter.ui.mainscreen.day.DayFragment
import kotlinx.android.synthetic.main.fragment_week.view.*
import kotlin.math.sign

class WeekFragment : Fragment() {
    val calendar: Calendar = Calendar.getInstance()
    lateinit var weekViewModel: WeekViewModel

    var currentMonth: Int = -1
    var firstDay: Int = -1
    var lastDay: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weekViewModel = ViewModelProvider(this).get(WeekViewModel::class.java)
        calendar.firstDayOfWeek = Calendar.MONDAY
        var week = arguments?.getInt("weekYear")
        if (week != null)
            calendar.set(Calendar.WEEK_OF_YEAR, week)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)

        currentMonth = arguments?.getInt("currentMonth") ?: calendar.get(Calendar.MONTH) + 1

        val weekNumber: Int = calendar.get(Calendar.WEEK_OF_YEAR)

        view.tvWeekN.text = "Week $weekNumber"
        val calendarDate = calendar

        weekViewModel.setDays(calendarDate, currentMonth, view).forEach { day ->
            childFragmentManager.beginTransaction().add(day.first, day.second).commit()
        }

        return view
    }

    fun previousWeek(view: View? = null) {
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        updateWeek()
    }

    fun nextWeek(view: View? = null) {
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        updateWeek()
    }

    fun updateWeek() {
        val week = calendar.get(Calendar.WEEK_OF_YEAR)

        view?.let {view ->
            childFragmentManager.fragments.forEach{
                childFragmentManager.beginTransaction().remove(it).commit()
            }
            weekViewModel.setDays(calendar, currentMonth, view).forEach { day ->
                childFragmentManager.beginTransaction().add(day.first, day.second).commit()
            }
            view.findViewById<TextView>(R.id.tvWeekN).text = "Week ${calendar.get(Calendar.WEEK_OF_YEAR)}"
        }

    }
}

class WeekGestureListener(val weekFragment: WeekFragment, val dayFragment: DayFragment?) :
    GestureDetector.SimpleOnGestureListener() {
    val FLING_THRESHOLD = 20

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val flingDistance = (e1?.x ?: 0f) - (e2?.x ?: 0f)

        if (Math.abs(flingDistance) < FLING_THRESHOLD)
            return true

        if (flingDistance.sign == 1.0f)
            weekFragment.nextWeek()

        if (flingDistance.sign == -1.0f)
            weekFragment.previousWeek()
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {

        return super.onDoubleTap(e)
    }

    override fun onLongPress(e: MotionEvent?) {
        if (dayFragment != null)
            dayFragment.openDialog()
        super.onLongPress(e)
    }
}