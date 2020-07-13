package com.faffo.shifter.ui.mainscreen.month

import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faffo.shifter.R
import com.faffo.shifter.ui.mainscreen.MainActivity
import com.faffo.shifter.ui.mainscreen.day.DayFragment
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.math.sign

class MonthFragment : Fragment() {
    val calendar: Calendar = Calendar.getInstance()
    var firstWeek: Int = -1
    var lastWeek: Int = -1
    var currentMonth = -1

    lateinit var monthViewModel: MonthViewModel


    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        monthViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_month, container, false)

        mDetector = GestureDetectorCompat(
            view.context,
            MonthGestureListener(this, null)
        )

        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mDetector.onTouchEvent(event)
            }
        })

        monthViewModel.setUpView(calendar).forEach { weekFragment ->
            childFragmentManager.beginTransaction().add(weekFragment.first, weekFragment.second)
                .commit()
        }
        setToolBarMonthText()
        return view
    }

//

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
        (activity as MainActivity).supportActionBar?.title =
            Month.of(calendar.get(Calendar.MONTH) + 1).getDisplayName(
                TextStyle.FULL,
                Locale.ITALY
            ).capitalize()
    }

    fun updateMonth() {
        monthViewModel.setUpView(calendar).forEach { weekFragment ->
            childFragmentManager.beginTransaction().replace(weekFragment.first, weekFragment.second)
                .commit()
        }

        setToolBarMonthText()
        parentFragmentManager.beginTransaction().replace(R.id.drawer_content, this)
            .commit()
    }

}

class MonthGestureListener(val monthFragment: MonthFragment, val dayFragment: DayFragment?) :
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