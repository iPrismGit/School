package com.iprism.school.activities.calender

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.databinding.ActivityCalenderBinding
import com.iprism.school.model.Request.TeacherCalenderlistReq
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class CalenderActivity : BaseActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private val viewModel: Scl_ViewModel by viewModels()

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var displayedDate: LocalDate = LocalDate.now()
    private lateinit var adapter: CalenderAdapter

    private val apiDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // userDetails and user are inherited from BaseActivity
        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        setupListeners()
        updateUIAndFetch()
    }

    private fun setupListeners() {
        binding.backIv.setOnClickListener {
            finish()
        }

        binding.leftArrowIv.setOnClickListener {
            displayedDate = displayedDate.minusMonths(1)
            updateUIAndFetch()
        }

        binding.rightArrowIv.setOnClickListener {
            displayedDate = displayedDate.plusMonths(1)
            updateUIAndFetch()
        }

        binding.addCalenderBtn.setOnClickListener {
            val intent = Intent(this, CreateCalenderActivity::class.java)
            intent.putExtra("tag", "create")
            startActivity(intent)
        }
    }

    private fun updateUIAndFetch() {
        // Update Month-Year display text (e.g., "February 2025")
        binding.dateTxt.text = displayedDate.format(displayDateFormatter)

        // Fetch data for the current displayed month.
        // We use the first day of the month as the representative date for fetching the list.
        val formattedDateString = displayedDate.withDayOfMonth(1).format(apiDateFormatter)
        fetchCalenderList(formattedDateString)
    }

    private fun fetchCalenderList(dateString: String) {
        showProgress()
        val apiRequest = TeacherCalenderlistReq(auth_token, dateString, scl_id, teacherId)
        Log.d("CalenderActivity", "Request: $apiRequest")

        viewModel.teacherCalenderList(apiRequest).observe(this, Observer { response ->
            hideProgress()
            if (response != null && response.status == true) {
                binding.nodataTv.visibility = View.GONE
                binding.calendersRv.visibility = View.VISIBLE

                val calenderDetails = response.response.calender_details ?: emptyList()
                adapter = CalenderAdapter(this, calenderDetails)
                binding.calendersRv.layoutManager = LinearLayoutManager(this)
                binding.calendersRv.adapter = adapter

                adapter.OnItemCallPic = { data ->
                    val intent = Intent(this, CalenderDetailsActivity::class.java)
                    intent.putExtra("calenderId", data.id.toString())
                    startActivity(intent)
                }
            } else {
                binding.nodataTv.visibility = View.VISIBLE
                binding.calendersRv.visibility = View.GONE

                if (response?.message == "Authentication Token Expired") {
                    user?.storeUserDetails("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        })
    }
}
