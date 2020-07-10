package com.faffo.shifter.widgets

import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faffo.shifter.AddShiftDialogFragment
import com.faffo.shifter.DayViewModel
import com.faffo.shifter.R
import com.faffo.shifter.ShiftType
import com.faffo.shifter.data.entities.Holiday
import com.faffo.shifter.data.entities.Shift
import kotlinx.android.synthetic.main.fragment_day.view.*
import kotlinx.coroutines.runBlocking

public class DayFragment() : Fragment(), DialogInterface.OnClickListener {
    val calendar = Calendar.getInstance()
    val today = Calendar.getInstance()
    var currentMonth: Int = -1

    lateinit var dayViewModel: DayViewModel

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentMonth = requireArguments().getInt("currentMonth")

        calendar.set(Calendar.YEAR, requireArguments().getInt("year"))
        calendar.set(Calendar.MONTH, requireArguments().getInt("month"))
        calendar.set(Calendar.DAY_OF_MONTH, requireArguments().getInt("day"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        mDetector = GestureDetectorCompat(view.context, DayGestureListener(this))
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                //openDialog()
                return mDetector.onTouchEvent(event)
            }
        })

        dayViewModel = ViewModelProvider(this).get(DayViewModel::class.java)

        setupView(view)

        return view
    }

    fun setupView(view: View) {
        val dayNumber: Int = calendar.get(Calendar.DAY_OF_MONTH)
        view.tvDayNumber.text = dayNumber.toString()


        if (calendar.get(Calendar.MONTH) != currentMonth) {
            view.tvDayNumber.setTextColor(
                AppCompatResources.getColorStateList(
                    view.context,
                    R.color.colorInactive
                )
            )
            view.background =
                AppCompatResources.getDrawable(view.context, R.drawable.day_border_inactive)?.let {
                    DrawableCompat.wrap(it)
                }
        }

        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> {
                view.background =
                    AppCompatResources.getDrawable(view.context, R.drawable.holiday_border)?.let {
                        DrawableCompat.wrap(it)
                    }
                view.tvDayNumber.setTextColor(resources.getColor(R.color.colorHoliday, null))
            }
        }

        var holiday: List<Holiday> = runBlocking {
            dayViewModel.getHolidayByMonthDay(
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        if (holiday.count() > 0) {
            view.background =
                AppCompatResources.getDrawable(view.context, R.drawable.holiday_border)?.let {
                    DrawableCompat.wrap(it)
                }
            view.tvDayNumber.setTextColor(resources.getColor(R.color.colorHoliday, null))
        }


        if ((calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR))
            && (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
        ) {
            view.background =
                AppCompatResources.getDrawable(view.context, R.drawable.day_border_today)?.let {
                    DrawableCompat.wrap(it)
                }
        }
        val shifts: List<Shift> = runBlocking {
            dayViewModel.getShifts(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        if (shifts.any()) {
            when (shifts[0].type) {
                ShiftType.FULL.ordinal -> {
                    view.background =
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.day_border_full_shift
                        )?.let {
                            DrawableCompat.wrap(it)
                        }
                }
                ShiftType.HALF.ordinal -> {
                    view.background =
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.day_border_half_shift
                        )?.let {
                            DrawableCompat.wrap(it)
                        }
                }
            }

        }
    }

    fun openDialog() {
        val dialogFragment = AddShiftDialogFragment(this)

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

        view?.let { setupView(it) }
        parentFragmentManager.beginTransaction().replace(this.id, this)
            .commit()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        TODO("Not yet implemented")
    }
}

class DayGestureListener(val dayFragment: DayFragment) :
    GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        dayFragment.openDialog()
        return true //super.onSingleTapConfirmed(e)
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        //return super.onDown(e)
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return false
    }


}