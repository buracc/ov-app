package dev.burak.ovapp.ui.main.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import dev.burak.ovapp.R
import dev.burak.ovapp.ui.main.MainActivity
import java.time.LocalTime
import java.util.*

class TimePickerFragment(
    val mainActivity: MainActivity
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(mainActivity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        mainActivity.findViewById<TextView>(R.id.ptTime)?.text = LocalTime.of(hourOfDay, minute).toString()
    }
}
