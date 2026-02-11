package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R
import com.iprism.school.activities.album.CreateDayCareAlbumsActivity
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityInitiateMessageBinding
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.messagemodel.MessageThread
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.StudentsViewModel
import com.iprism.school.viewModels.ViewModelFactory

class InitiateMessageActivity : BaseActivity() {

    private lateinit var binding: ActivityInitiateMessageBinding
    private lateinit var viewModel: MessagesViewModel
    private lateinit var attendanceViewModel: AttendanceViewModel
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private val studentList = ArrayList<Student?>()
    private val selectedStudentIds = mutableListOf<String>()
    private var selectedValue = ""
    private var selectedId = ""
    private var selectedName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInitiateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        observeClassesResponse()
        observeSectionsResponse()
        observeSendMessageResponse()
        handleSentBtn()
        handleBack()
        handleSelectStudentsLo()
        var requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
    }

    private fun handleSelectStudentsLo() {
        binding.selectStudentsLo.setOnClickListener { view ->
            if (classId.equals("-1", true)){
                ToastUtils.showErrorCustomToast(this, "Please Select Class..!")
            } else if (sectionId.equals("-1", true)){
                ToastUtils.showErrorCustomToast(this, "Please Select Section..!")
            } else {
                val dialog = StudentSelectDialogFragment(
                    classId = classId,
                    sectionId = sectionId,
                    studentList = studentList,
                    alreadySelectedIds = selectedStudentIds
                ) { value, id, name ->

                    when (value) {
                        "Broadcast" -> {
                            selectedValue = "Broadcast"
                            selectedId = "0"
                            selectedName = ""

                            binding.selectStudentTxt.text = "All Students Message"
                        }

                        "single" -> {
                            selectedValue = "single"
                            selectedId = id
                            selectedName = name

                            binding.selectStudentTxt.text = name
                        }

                        "Multiple" -> {
                            selectedValue = "Multiple"
                            selectedId = ""
                            selectedName = ""

                            binding.selectStudentTxt.text = "Multiple Students Selected"
                        }
                    }
                }

                dialog.show(supportFragmentManager, "StudentSelectDialog")

            }
        }
    }

    private fun handleBack() {
        binding.ivBack.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleSentBtn() {
        binding.sendBtn.setOnClickListener { view ->

        }
    }

    private fun initViewModel() {
        val repository = MessagesRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory { MessagesViewModel(repository) })[MessagesViewModel::class.java]

        val attendanceRepository = AttendanceRepository(this)
        val attendanceFactory = ViewModelFactory { AttendanceViewModel(attendanceRepository) }
        attendanceViewModel = ViewModelProvider(this, attendanceFactory)[AttendanceViewModel::class.java]

    }

    private fun observeSendMessageResponse() {
        viewModel.insertMessageResponse.observe(this) { result ->
            when (result) {

                is UiState.Loading ->
                    binding.progress.showProgress()

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("tag", "Message Sent ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
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
                    if (!sectionId.equals("-1", true)) {
                        //loadStudents()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

}