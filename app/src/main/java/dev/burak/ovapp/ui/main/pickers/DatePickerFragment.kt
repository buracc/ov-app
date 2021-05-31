package dev.burak.ovapp.ui.main.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import dev.burak.ovapp.R
import dev.burak.ovapp.ui.main.MainActivity
import dev.burak.ovapp.util.FormatUtils
import java.time.LocalDate
import java.util.*

class DatePickerFragment(
    val mainActivity: MainActivity
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(mainActivity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        mainActivity.findViewById<TextView>(R.id.ptDate)?.text =
            LocalDate.of(year, month + 1, day).format(FormatUtils.dateFormatter)
    }
}
