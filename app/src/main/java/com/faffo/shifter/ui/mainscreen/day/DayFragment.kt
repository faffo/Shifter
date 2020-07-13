package com.faffo.shifter.ui.mainscreen.day

import android.content.DialogInterface
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faffo.shifter.R
import com.faffo.shifter.data.entities.CalendarEvent
import com.faffo.shifter.data.entities.Shift
import com.faffo.shifter.ui.dialogs.AddShiftDialogFragment
import com.faffo.shifter.ui.mainscreen.MainActivity
import com.faffo.shifter.ui.mainscreen.month.MonthFragment
import com.faffo.shifter.ui.mainscreen.month.MonthGestureListener
import com.faffo.shifter.ui.mainscreen.week.WeekFragment
import com.faffo.shifter.ui.mainscreen.week.WeekGestureListener
import com.faffo.shifter.utils.ShiftType
import kotlinx.coroutines.runBlocking
import kotlin.math.sign

class DayFragment : Fragment(), DialogInterface.OnClickListener {
    val calendar = Calendar.getInstance()
    val today = Calendar.getInstance()

    var currentMonth: Int = -1
    var year: Int = -1
    var month: Int = -1
    var day: Int = -1

    var calendarEvents: List<CalendarEvent> = mutableListOf()

    lateinit var settings: SharedPreferences

    lateinit var dayViewModel: DayViewModel

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        year = arguments?.getInt("year") ?: today.get(Calendar.YEAR)
        month = arguments?.getInt("month") ?: today.get(Calendar.MONTH)
        day = arguments?.getInt("day") ?: today.get(Calendar.DAY_OF_MONTH)
        currentMonth = arguments?.getInt("currentMonth") ?: month + 1

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this.context)

        val monthFragment: MonthFragment
        val weekFragment: WeekFragment
        mDetector = GestureDetectorCompat(
            view.context,
            DayGestureListener(this)
        )
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mDetector.onTouchEvent(event)
            }
        })

        dayViewModel = ViewModelProvider(this).get(DayViewModel::class.java)
        dayViewModel.setupView(calendar, currentMonth, today, view)

        return view
    }

    fun openDialog() {
        val dialogFragment =
            AddShiftDialogFragment(this)

        dialogFragment.show(this.childFragmentManager, "pippo!")
    }

    fun onDialogPositiveClick(
        addShiftDialogFragment: AddShiftDialogFragment,
        shiftType: ShiftType
    ) {
        val shift = Shift(
            0,
            shiftType.ordinal,
            shiftType.hours,
            employee = "Daniele Ruffini",
            day = calendar.get(Calendar.DAY_OF_MONTH),
            month = calendar.get(Calendar.MONTH),
            year = calendar.get(Calendar.YEAR),
            color = null
        )
        runBlocking { dayViewModel.saveShift(shift) }

        view?.let { dayViewModel.setupView(calendar, currentMonth, today, it) }
        parentFragmentManager.beginTransaction().replace(this.id, this)
            .commit()
    }

    fun previousDay(view: View? = null) {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        updateDay()
    }

    fun nextDay(view: View? = null) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        updateDay()
    }

    fun updateDay() {
        //setUpView(true)
        val v = view

        view?.let { view ->
            dayViewModel.setupView(calendar, currentMonth, today, view)
            parentFragmentManager.beginTransaction().replace(this.id, this)
                .commit()
        }

    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        return
    }
}

class DayGestureListener(val dayFragment: DayFragment?) :
    GestureDetector.SimpleOnGestureListener() {
    val FLING_THRESHOLD = 20

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (dayFragment != null) {
            if (dayFragment.parentFragment?.parentFragment != null
                && dayFragment.parentFragment?.parentFragment is MonthFragment
            ) {
                val monthFragment: MonthFragment =
                    dayFragment.parentFragment?.parentFragment as MonthFragment
                val monthGestureViewer = MonthGestureListener(monthFragment, dayFragment)
                return monthGestureViewer.onFling(e1, e2, velocityX, velocityY)
            } else if (dayFragment.parentFragment != null && dayFragment.parentFragment is WeekFragment) {
                val weekFragment: WeekFragment = dayFragment.parentFragment as WeekFragment
                val weekGestureListener = WeekGestureListener(weekFragment, dayFragment)
                return weekGestureListener.onFling(e1, e2, velocityX, velocityY)
            } else {
                val flingDistance = (e1?.x ?: 0f) - (e2?.x ?: 0f)

                if (Math.abs(flingDistance) < FLING_THRESHOLD)
                    return true

                if (flingDistance.sign == 1.0f)
                    dayFragment.nextDay()

                if (flingDistance.sign == -1.0f)
                    dayFragment.previousDay()
                return false
            }
        }

        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        //if dayFragment != null it means this event has been fired by the dayFragment.
        if (dayFragment != null && dayFragment.activity != null && dayFragment.activity is MainActivity) {
            var mainActivity = dayFragment.activity as MainActivity
            mainActivity.replaceContent(R.id.nav_day, fragment = dayFragment)
        }
        return super.onDoubleTap(e)
    }

    override fun onLongPress(e: MotionEvent?) {
        if (dayFragment != null)
            dayFragment.openDialog()
        super.onLongPress(e)
    }
}