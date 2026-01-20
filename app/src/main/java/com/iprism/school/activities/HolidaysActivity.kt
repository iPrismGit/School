package com.iprism.school.activities

import TodayDecorator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityHolidaysBinding
import com.iprism.school.databinding.DialogHolidayBinding
import com.iprism.school.model.holidaysmodel.Holiday
import com.iprism.school.model.holidaysmodel.HolidaysApiRequest
import com.iprism.school.repositories.HolidaysRepository
import com.iprism.school.utils.HolidayDecorator
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.HolidaysViewModel
import com.iprism.school.viewModels.ViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HolidaysActivity : BaseActivity() {

    private lateinit var binding: ActivityHolidaysBinding
    private lateinit var holidaysViewModel: HolidaysViewModel
    private var currentHolidayList: List<Holiday> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHolidaysBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        observeHolidaysCalenderResponse()
        loadCurrentMonthHolidays()
        handleMonthChanging()
        handleBack()
        binding.calendarView.setOnDateChangedListener { widget, date, _ ->

            val clickedDate = String.format(
                Locale.getDefault(),
                "%04d-%02d-%02d",
                date.year,
                date.month + 1,
                date.day
            )

            val holiday = currentHolidayList.firstOrNull {
                it.date == clickedDate && it.status == "holiday"
            }

            if (holiday != null) {
                showHolidayPopup(widget, holiday.title)
            }
        }


    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleMonthChanging() {
        binding.calendarView.setOnMonthChangedListener { _, date ->
            val selectedMonth = date.month + 1
            val selectedYear = date.year

            callHolidayApi(selectedMonth, selectedYear)
        }
    }

    private fun loadCurrentMonthHolidays() {
        val calendar = Calendar.getInstance()

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        callHolidayApi(month, year)
    }

    private fun callHolidayApi(month: Int, year: Int) {

        val request = HolidaysApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            month.toString(),
            userDetails[User.ID].toString(),
            year.toString()
        )

        holidaysViewModel.fetchHolidays(request)
    }

    private fun observeHolidaysCalenderResponse() {
        holidaysViewModel.holidaysResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()

                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    currentHolidayList = result.data.holidays
                    val holidayDates = getHolidayDates(currentHolidayList)
                    binding.calendarView.removeDecorators()
                    binding.calendarView.addDecorator(TodayDecorator(this))
                    binding.calendarView.addDecorator(HolidayDecorator(this, holidayDates))

                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()

                }
            }
        }
    }

    private fun showHolidayPopup(anchorView: View, holidayTitle: String) {

        val binding = DialogHolidayBinding.inflate(layoutInflater)
        binding.tvHolidayMessage.text = holidayTitle

        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        anchorView.post {
            popupWindow.showAtLocation(
                anchorView.rootView,
                Gravity.CENTER,
                0,
                0
            )
        }
    }


    private fun initViewModel() {
        val eventsRepository = HolidaysRepository(this)
        val eventsFactory = ViewModelFactory { HolidaysViewModel(eventsRepository) }
        holidaysViewModel = ViewModelProvider(this, eventsFactory)[HolidaysViewModel::class.java]
    }

    private fun getHolidayDates(holidays: List<Holiday>): Set<CalendarDay> {

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        return holidays
            .filter { it.status == "holiday" }
            .mapNotNull { holiday ->
                try {
                    val date = sdf.parse(holiday.date) ?: return@mapNotNull null
                    calendar.time = date
                    CalendarDay.from(calendar)
                } catch (e: Exception) {
                    null
                }
            }
            .toSet()
    }

}