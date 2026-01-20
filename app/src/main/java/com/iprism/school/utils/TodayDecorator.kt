import android.content.Context
import androidx.core.content.ContextCompat
import com.iprism.school.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(
    private val context: Context
) : DayViewDecorator {

    private val today = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == today
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.bg_today
            )!!
        )
    }
}
