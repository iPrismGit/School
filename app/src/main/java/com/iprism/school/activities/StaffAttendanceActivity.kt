package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.utils.AbsentDayDecorator
import com.iprism.school.utils.PresentDayDecoration
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiRequest
import com.iprism.school.repositories.StaffAttendanceApiRepository
import com.iprism.school.utils.HolidayDecorator
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.StaffAttendanceViewModel
import com.iprism.school.viewModels.ViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar
import java.util.HashMap

class StaffAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffAttendanceBinding
    private lateinit var attendanceViewModel: StaffAttendanceViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStaffAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        user = User(this)
        userDetails = user.getNewUserDetails()
        handleAddBtn()
        initViewModel()
        observeStudentAttendanceResponse()
        loadCurrentMonthAttendance()
        handleMonthChanging()
        handleBackBtn()
        binding.calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_NONE
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endOfCurrentMonth = CalendarDay.from(calendar)
        binding.calendarView.state().edit()
            .setMaximumDate(endOfCurrentMonth)
            .commit()
        binding.calendarView.isPagingEnabled = false

    }

    private fun handleBackBtn() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleAddBtn() {
        binding.addAttendanceBtn.setOnClickListener { view ->
            startActivity(Intent(this, AddAttendanceActivity::class.java))
        }
    }

    private fun initViewModel() {
        val eventsRepository = StaffAttendanceApiRepository(this)
        val eventsFactory = ViewModelFactory { StaffAttendanceViewModel(eventsRepository) }
        attendanceViewModel =
            ViewModelProvider(this, eventsFactory)[StaffAttendanceViewModel::class.java]
    }

    private fun loadCurrentMonthAttendance() {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        callAttendanceApi(month, year)
    }

    private fun handleMonthChanging() {
        binding.calendarView.setOnMonthChangedListener { _, date ->
            val selectedMonth = date.month + 1
            val selectedYear = date.year

            callAttendanceApi(selectedMonth, selectedYear)
        }
    }

    private fun callAttendanceApi(month: Int, year: Int) {
        val request = StaffAttendanceApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            "",
            "",
            "",
            month.toString(),
            "",
            "",
            "",
            userDetails[User.ID].toString(),
            "view",
            year.toString(),
            ""
        )

        Log.d("AttendanceDetails", request.toString())
        attendanceViewModel.staffAttendanceDetails(request)
    }

    private fun parseDate(date: String): CalendarDay {
        val parts = date.split("-")
        return CalendarDay.from(
            parts[0].toInt(),
            parts[1].toInt() - 1,
            parts[2].toInt()
        )
    }

    private fun observeStudentAttendanceResponse() {
        attendanceViewModel.attendanceDetailsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.mainLo.visibility = View.VISIBLE

                    binding.countTxt.text = result.data.total_days
                    binding.presentCountTxt.text = result.data.present_days
                    binding.absentCountTxt.text = result.data.absent_days
                    binding.holidaysCountTxt.text = result.data.holidays

                    val presentDates = mutableSetOf<CalendarDay>()
                    val holidayDates = mutableSetOf<CalendarDay>()
                    val absentDates = mutableSetOf<CalendarDay>()

                    result.data.attendance.forEach { day ->
                        val calendarDay = parseDate(day.date)

                        when (day.status.lowercase()) {
                            "present" -> presentDates.add(calendarDay)
                            "holiday" -> holidayDates.add(calendarDay)
                            "absent" -> absentDates.add(calendarDay)
                        }
                    }

                    binding.calendarView.removeDecorators()

                    binding.calendarView.addDecorators(
                        PresentDayDecoration(this, presentDates),
                        HolidayDecorator(this, holidayDates),
                        AbsentDayDecorator(this, absentDates)
                    )
                }


                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                    binding.mainLo.visibility = View.GONE

                }
            }
        }
    }

}