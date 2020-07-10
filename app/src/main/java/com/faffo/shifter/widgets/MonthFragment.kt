package com.faffo.shifter.widgets

import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.faffo.shifter.R
import com.faffo.shifter.ShifterActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.math.sign

class MonthFragment : Fragment(), View.OnClickListener {
    val calendar: Calendar = Calendar.getInstance()
    var firstWeek: Int = -1
    var lastWeek: Int = -1
    var currentMonth = -1


    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calendar.set(Calendar.DAY_OF_MONTH, 1)

//        calendar.set(Calendar.YEAR, requireArguments().getInt("year"))
//        calendar.set(Calendar.MONTH, requireArguments().getInt("month"))

        //currentMonth = calendar.get(Calendar.MONTH)

        //val weekCalendar = calendar

        //weekCalendar.set(Calendar.DAY_OF_MONTH, 1)
        //firstWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_month, container, false)

        mDetector = GestureDetectorCompat(view.context, MonthGestureListener(this))

        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mDetector.onTouchEvent(event)
            }
        })

        setUpView()

        var addShiftbutton = view.findViewById<FloatingActionButton>(R.id.fab_add_shift)
        addShiftbutton.setOnClickListener(this)

        return view //inflater.inflate(R.layout.fragment_month, container)
    }

    private fun setUpView(replace: Boolean = false) {
        firstWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        for (i: Int in firstWeek..firstWeek + 5) {
            val weekCalendar = Calendar.getInstance()
            weekCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            weekCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            weekCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))

            weekCalendar.set(Calendar.WEEK_OF_YEAR, i)
            val weekofMonth = i % firstWeek + 1 //weekCalendar.get(Calendar.WEEK_OF_MONTH)
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

            var week = WeekFragment()
            week.arguments = bundle

            if (replace)
                childFragmentManager.beginTransaction().replace(id, week).commit()
            else
                childFragmentManager.beginTransaction().add(id, week).commit()
        }
        setToolBarMonthText()
    }

    fun previousMonth(view: View? = null) {
        calendar.add(Calendar.MONTH, -1)
        updateMonth()
    }

    fun nextMonth(view: View? = null) {
        calendar.add(Calendar.MONTH, 1)
        updateMonth()
    }

    private fun setToolBarMonthText() {
        var date = calendar.time
        //tvToolBarMonth.text = Month.of(calendarDate.get(Calendar.MONTH) + 1).getDisplayName(
        (activity as ShifterActivity).supportActionBar?.title =
            Month.of(calendar.get(Calendar.MONTH) + 1).getDisplayName(
                TextStyle.FULL,
                Locale.ITALY
            )
                .capitalize() //calendarDate.getDisplayName(ULocale.ITALY) DateFormat.getDateInstance(DateFormat.MONTH_FIELD, Locale.ITALY)
    }

    fun updateMonth() {
        setUpView(true)
        parentFragmentManager.beginTransaction().replace(R.id.fragment_drawer_content, this)
            .commit()
    }

    override fun onClick(v: View?) {
//        val builder: AlertDialog.Builder? = activity?.let {
//            AlertDialog.Builder(it)
//        }
//
//        builder?.setMessage("POROCO???")
//            ?.setTitle("GNE")
//
//        val dialog: AlertDialog? = builder?.create()

        //val dialogFragment = AddShiftDialogFragment()
        //dialogFragment.show(this.childFragmentManager, "pippo!")
    }

}

class MonthGestureListener(val monthFragment: MonthFragment) :
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
            monthFragment.nextMonth()

        if (flingDistance.sign == -1.0f)
            monthFragment.previousMonth()
        //return super.onFling(e1, e2, velocityX, velocityY)
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        //return super.onDown(e)
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return super.onDoubleTap(e)
    }

    override fun onLongPress(e: MotionEvent?) {
        //super.onLongPress(e)
    }

}