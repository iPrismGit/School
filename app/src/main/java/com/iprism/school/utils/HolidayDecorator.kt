package com.iprism.school.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.iprism.school.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class HolidayDecorator(
    private val context: Context,
    private val dates: Set<CalendarDay>
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.bg_holiday
            )!!
        )
    }
}

