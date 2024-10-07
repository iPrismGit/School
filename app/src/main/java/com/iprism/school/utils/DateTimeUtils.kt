package com.iprism.school.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import java.util.Calendar
import java.util.Locale

object DateTimeUtils {

    var dateMonthYear: String = ""

    fun getDate(dateTxt: TextView, isPreviousCalendar: Boolean): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            dateTxt.context,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                c.set(year, monthOfYear , dayOfMonth)

                // Format the date to "dd MMM, yyyy"
                val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                val formattedDate = sdf.format(c.time)
                dateTxt.text = formattedDate
                dateMonthYear = "$year-${monthOfYear + 1}-$dayOfMonth"
            },
            year, month, day
        )

        if (isPreviousCalendar) {
            datePickerDialog.datePicker.maxDate = c.timeInMillis
        } else {
            datePickerDialog.datePicker.minDate = c.timeInMillis
        }
        datePickerDialog.show()

        return dateMonthYear
    }

    @SuppressLint("SetTextI18n")
    fun getTime(timeTxt: TextView) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            timeTxt.context,
            { _: TimePicker, hourOfDay: Int, selectedMinute: Int ->
                val amPm: String
                var hourToShow = hourOfDay
                when {
                    hourToShow == 0 -> {
                        amPm = "AM"
                        hourToShow = 12
                    }

                    hourToShow == 12 -> amPm = "PM"
                    hourToShow > 11 -> {
                        amPm = "PM"
                        hourToShow -= 12
                    }

                    else -> amPm = "AM"
                }

                val formattedTime = String.format(
                    Locale.getDefault(),
                    "%02d:%02d %s",
                    hourToShow,
                    selectedMinute,
                    amPm
                )
                timeTxt.text = formattedTime
            },
            hour, minute, false
        )
        timePickerDialog.show()
    }

}
