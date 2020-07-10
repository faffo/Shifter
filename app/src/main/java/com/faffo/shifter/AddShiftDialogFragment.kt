package com.faffo.shifter

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.faffo.shifter.widgets.DayFragment

class AddShiftDialogFragment(val dayFragment: DayFragment) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)

            val view = layoutInflater.inflate(R.layout.dialog_shift, null)

            builder.setView(view)

            builder.setMessage("Add Shift")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the positive button event back to the host activity
                        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupShift)

                        var shiftType: ShiftType = ShiftType.FULL

                        when (radioGroup.checkedRadioButtonId) {
                            R.id.radioButtonFullShift -> shiftType = ShiftType.FULL
                            R.id.radioButtonHalfShift -> shiftType = ShiftType.HALF
                        }

                        dayFragment.onDialogPositiveClick(this, shiftType)
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the negative button event back to the host activity
                        //dayFragment.onDialogNegativeClick(this)
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}