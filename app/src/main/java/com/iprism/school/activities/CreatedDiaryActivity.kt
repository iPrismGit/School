package com.iprism.school.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.CreatedDiariesAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityCreatedDiaryBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.interfaces.OnCreatedDiariesClickListener
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.dairy.Diary
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.DiaryRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.DiaryViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.util.Locale

class CreatedDiaryActivity : BaseActivity() {

    private lateinit var binding: ActivityCreatedDiaryBinding
    private val calendar: Calendar = Calendar.getInstance()

    private val displayDateFormat =
        SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

    private val backendDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var diariesViewModel: DiaryViewModel
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var diariesAdapter: CreatedDiariesAdapter
    private var diariesList = mutableListOf<Diary>()
    private var isFreshLoad = false
    private var backendDate = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private lateinit var bottomSheetDialog : BottomSheetDialog
    private lateinit var deleteBinding : DeleteBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatedDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDate()
        handleArrowClicks()
        handleBack()
        initViewModel()
        setupRecyclerView()
        observeEventsResponse()
        handleRefreshLo()
        observeClassesResponse()
        observeSectionsResponse()
        observeDeleteDiaryResponse()
        var requestClasses = ClassTeacherApiRequest("", userDetails[User.ID].toString(),userDetails[User.SCHOOL_ID].toString(),userDetails[User.ACADEMIC_YEAR_ID].toString(), "classes")
        attendanceViewModel.fetchClasses(requestClasses)
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(this)
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val diariesRepository = DiaryRepository(this)
        val diariesFactory = ViewModelFactory { DiaryViewModel(diariesRepository) }
        diariesViewModel = ViewModelProvider(this, diariesFactory)[DiaryViewModel::class.java]
    }

    private fun loadDiaries(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            diariesList.clear()
            diariesAdapter.notifyDataSetChanged()

            binding.diariesRv.visibility = View.GONE
            binding.noDataFoundLo.visibility = View.VISIBLE
        }

        isLoading = true

        val request = DiaryApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            classId,
            backendDate,
            "",
            "",
            "",
            currentPage,
            sectionId,
            "",
            "",
            "",
            userDetails[User.ID].toString(),
            "view"
        )

        Log.d("DiariesApiRequest", request.toString())
        diariesViewModel.fetchDiaries(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        diariesList.clear()
        diariesAdapter.notifyDataSetChanged()
        loadDiaries(isFromFilterChange = true)
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadDiaries()
    }

    private fun resetEvents() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        diariesList.clear()
        diariesAdapter.notifyDataSetChanged()
        binding.diariesRv.visibility = View.GONE
        binding.noDataFoundLo.visibility = View.VISIBLE
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshItems()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun setupRecyclerView() {
        diariesAdapter = CreatedDiariesAdapter(this, diariesList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.diariesRv.apply {
            layoutManager = linearLayoutManager
            adapter = diariesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.diariesRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && diariesList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })

            diariesAdapter.setListener(object  : OnCreatedDiariesClickListener{
                override fun onDeleteClickListener(dairyId: String) {
                    showDeleteBottomSheet(dairyId)
                }

                override fun onInformationClickListener(
                    studentId: String,
                    image: String,
                    type: String,
                    details: String,
                    firstName: String,
                    middleName: String,
                    lastName: String
                ) {
                    var intent = Intent(this@CreatedDiaryActivity, DiaryDetailsActivity::class.java)
                    intent.putExtra("StudentId", studentId)
                    intent.putExtra("image", image)
                    intent.putExtra("type", type)
                    intent.putExtra("details", details)
                    intent.putExtra("firstName", firstName)
                    intent.putExtra("middleName", middleName)
                    intent.putExtra("lastName", lastName)
                    startActivity(intent)
                }

            })
        }

    }

    private fun observeEventsResponse() {
        diariesViewModel.diaryResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newDiaries = result.data.diaries

                    if (newDiaries.isNotEmpty()) {

                        if (isFreshLoad) {
                            diariesList.clear()
                            isFreshLoad = false
                        }

                        diariesList.addAll(newDiaries)
                        diariesAdapter.notifyDataSetChanged()

                        binding.diariesRv.visibility = View.VISIBLE
                        binding.noDataFoundLo.visibility = View.GONE

                        isLastPage = newDiaries.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            diariesList.clear()
                            diariesAdapter.notifyDataSetChanged()
                            binding.diariesRv.visibility = View.GONE
                            binding.noDataFoundLo.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (diariesList.isEmpty()) {
                        binding.diariesRv.visibility = View.GONE
                        binding.noDataFoundLo.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.diariesRv.visibility = View.VISIBLE
                        binding.noDataFoundLo.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun observeDeleteDiaryResponse() {
        diariesViewModel.deleteDiaryResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    deleteBinding.progress.showProgress()
                    deleteBinding.deleteButton.isEnabled = false
                    deleteBinding.cancelBtn.isEnabled = false
                    deleteBinding.crossIv.isEnabled = false

                }

                is UiState.Success -> {
                    deleteBinding.progress.hideProgress()
                    ToastUtils.showSuccessCustomToast(this, "Diary Deleted Successfully..!")
                    refreshItems()
                    bottomSheetDialog.dismiss()

                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    deleteBinding.progress.hideProgress()
                    deleteBinding.deleteButton.isEnabled = true
                    deleteBinding.cancelBtn.isEnabled = true
                    deleteBinding.crossIv.isEnabled = true
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
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
                    resetEvents()
                    if (!sectionId.equals("-1", true)) {
                        loadDiaries()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun showDeleteBottomSheet(diaryId : String) {
        bottomSheetDialog = BottomSheetDialog(this)
        deleteBinding = DeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(deleteBinding.root)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        deleteBinding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBinding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBinding.deleteButton.setOnClickListener(View.OnClickListener {
            val request = DiaryApiRequest(
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                userDetails[User.SCHOOL_ID].toString(),
                classId,
                backendDate,
                "",
                diaryId,
                "",
                currentPage,
                sectionId,
                "",
                "",
                "",
                userDetails[User.ID].toString(),
                "delete"
            )

            Log.d("DeleteDiaryApiRequest", request.toString())
            diariesViewModel.deleteDiary(request)
        })

        bottomSheetDialog.show()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setDate() {
        updateDate()
    }

    private fun handleArrowClicks() {
        binding.leftArrowIv.setOnClickListener {
            changeDate(-1)
        }

        binding.rightArrowIv.setOnClickListener {
            changeDate(1)
        }
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        updateDate()
    }

    private fun updateDate() {
        val displayDate = displayDateFormat.format(calendar.time)
        binding.dateTxt.text = displayDate
        backendDate = backendDateFormat.format(calendar.time)

        Log.d("DisplayDate", displayDate)
        Log.d("BackendDate", backendDate)
        if (!classId.equals("-1", true) && !sectionId.equals("-1", true)) {
            refreshItems()
        }
    }

}