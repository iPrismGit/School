package com.iprism.school.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.school.model.timetable.TimeTableRequest
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityTimeTableBinding
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.TimeTableRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.TimeTableViewModel
import com.iprism.school.viewModels.ViewModelFactory

class TimeTableActivity : BaseActivity() {

    private lateinit var binding: ActivityTimeTableBinding
    private lateinit var viewModel: TimeTableViewModel
    private lateinit var attendanceViewModel: AttendanceViewModel
    private var classId: String = "-1"
    private var sectionId: String = "-1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener( binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        initViewModel()
        fetchTimeTable()
        setupObservers()
        observeClassesResponse()
        observeSectionsResponse()
        var requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun fetchTimeTable() {
        val request = TimeTableRequest(
            userDetails[User.ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            classId,
            sectionId,
            "1"
        )
        viewModel.fetchTimeTable(request)
        Log.d("requestLoading", request.toString())
    }

    private fun initViewModel() {
        val repository = TimeTableRepository(this)
        val factory = ViewModelFactory { TimeTableViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[TimeTableViewModel::class.java]

        val attendanceRepository = AttendanceRepository(this)
        val attendanceFactory = ViewModelFactory { AttendanceViewModel(attendanceRepository) }
        attendanceViewModel = ViewModelProvider(this, attendanceFactory)[AttendanceViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.response.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.noTimeTableTxt.visibility = View.GONE
                    binding.imageIv.visibility = View.VISIBLE
                    /*Glide.with(this)
                        .load(Constants.IMAGES_URL + state.data.response.timeTable[0].image)
                        .error(R.drawable.dummy_logo)
                        .listener(object : RequestListener<Drawable> {

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progress.visibility = View.GONE
                                return false
                            }
                        })
                        .into(binding.imageIv)*/
                    Glide.with(this).load(Constants.IMAGES_URL + state.data.response.timeTable[0].image).into(binding.imageIv)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (state.message.equals("no data found", true)) {
                        binding.noTimeTableTxt.visibility = View.VISIBLE
                        binding.imageIv.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeClassesResponse() {
        attendanceViewModel.classesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.classes.isNotEmpty()) {
                        var updatedList = result.data.classes.toMutableList()
                        updatedList.add(0, Class("-1", "Select Class"))
                        setupClassesAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeSectionsResponse() {
        attendanceViewModel.sectionsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.sections.isNotEmpty()) {
                        var updatedList = result.data.sections.toMutableList()
                        updatedList.add(0, Section("-1", "Select Section"))
                        setupSectionsAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.classesSp.adapter = adapter
        binding.classesSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    classId = genderTypes[position].class_id.toString()
                    if (!classId.equals("-1", true)) {
                        var requestClasses = ClassTeacherApiRequest(
                            classId,
                            userDetails[User.ID].toString(),
                            userDetails[User.SCHOOL_ID].toString(),
                            userDetails[User.ACADEMIC_YEAR_ID].toString(),
                            "sections"
                        )
                        attendanceViewModel.fetchSections(requestClasses)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupSectionsAdapter(genderTypes: List<Section>) {
        var namesList = genderTypes.map { it.section_name }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sectionsSp.adapter = adapter
        binding.sectionsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sectionId = genderTypes[position].section_id.toString()
                    fetchTimeTable()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }
}