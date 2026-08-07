package com.iprism.school.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.base.BaseFragment
import com.iprism.school.adapters.StudentsAdapter
import com.iprism.school.databinding.FragmentInActiveStudentsBinding
import com.iprism.school.interfaces.OnStudentClickListener
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.StudentsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.StudentsViewModel
import com.iprism.school.viewModels.ViewModelFactory

class InActiveStudentsFragment : BaseFragment() {

    private lateinit var binding: FragmentInActiveStudentsBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var studentsViewModel: StudentsViewModel
    private var studentsList = mutableListOf<Student>()
    private lateinit var studentsAdapter: StudentsAdapter
    private val CALL_PHONE_PERMISSION_CODE = 1
    private var mobileNumber: String = ""
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var classId: String = "-1"
    private var sectionId: String = "-1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInActiveStudentsBinding.inflate(inflater, container, false)
        initViewModel()
        observeClassesResponse()
        observeSectionsResponse()
        setupRecyclerView()
        observeEventsResponse()
        handleRefreshLo()
        val requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
        return binding.root
    }

    private fun observeClassesResponse() {
        attendanceViewModel.classesResponse.observe(viewLifecycleOwner) { result ->
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
                        ToastUtils.showErrorCustomToast(requireContext(), "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeSectionsResponse() {
        attendanceViewModel.sectionsResponse.observe(viewLifecycleOwner) { result ->
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
                        ToastUtils.showErrorCustomToast(requireContext(), "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namesList)
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
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namesList)
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
                    resetStudents()
                    if (!sectionId.equals("-1", true)) {
                        loadStudents()

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(requireContext())
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val studentsRepository = StudentsRepository(requireContext())
        val studentsFactory = ViewModelFactory { StudentsViewModel(studentsRepository) }
        studentsViewModel = ViewModelProvider(this, studentsFactory)[StudentsViewModel::class.java]
    }

    private fun loadStudents(isFromFilterChange: Boolean = false) {

        /*if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            studentsList.clear()
            studentsAdapter.notifyDataSetChanged()

            binding.studentsRv.visibility = View.GONE
            binding.noDataFoundTxt.visibility = View.VISIBLE
        }

        isLoading = true*/

        val request = StudentsApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            classId,
            currentPage,
            sectionId,
            userDetails[User.ID].toString()
        )

        Log.d("StudentsApiRequest", request.toString())
        studentsViewModel.fetchInActiveStudents(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()
        loadStudents(isFromFilterChange = true)
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        studentsAdapter.showLoadingFooter()
        loadStudents()
    }

    private fun resetStudents() {
        currentPage = 1
        isLastPage = false
        isLoading = false

        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()

        binding.studentsRv.visibility = View.GONE
        binding.noDataFoundTxt.visibility = View.VISIBLE
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
        studentsAdapter = StudentsAdapter(studentsList as ArrayList<Student?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.studentsRv.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.studentsRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && studentsList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })

            studentsAdapter.setupListener(object : OnStudentClickListener {
                override fun onCallClick(mobileNumber: String) {
                    this@InActiveStudentsFragment.mobileNumber = mobileNumber
                    if (mobileNumber.isNotEmpty()) {
                        makePhoneCall(this@InActiveStudentsFragment.mobileNumber)
                    }
                }

            })
        }

    }

    private fun observeEventsResponse() {
        studentsViewModel.inActiveStudentsResponse.observe(viewLifecycleOwner) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newStudents = result.data.students
                    if (newStudents.isNotEmpty()) {
                        studentsList.addAll(newStudents)
                        studentsAdapter.notifyDataSetChanged()
                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE
                        if (result.data.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }

                    /*val newEvents = result.data.students*/

                   /* if (newEvents.isNotEmpty()) {

                        if (isFreshLoad) {
                            studentsList.clear()
                            isFreshLoad = false
                        }

                        studentsList.addAll(newEvents)
                        studentsAdapter.notifyDataSetChanged()

                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE

                        isLastPage = newEvents.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            studentsList.clear()
                            studentsAdapter.notifyDataSetChanged()
                            binding.studentsRv.visibility = View.GONE
                            binding.noDataFoundTxt.visibility = View.VISIBLE
                        }
                    }*/

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    studentsAdapter.removeLoadingFooter()
                    if (studentsList.isEmpty()) {
                        binding.studentsRv.visibility = View.GONE
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    } else {
                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(requireContext(), "There is no more data")
                    }
                }
            }
        }
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )
            ) {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_PERMISSION_CODE
                )
            } else {

                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is permanently denied. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mobileNumber)
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is required to make phone calls. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

}