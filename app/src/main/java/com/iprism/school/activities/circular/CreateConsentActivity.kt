package com.iprism.school.activities.circular


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.ImageAdapter
import com.iprism.school.databinding.ActivityCreateConsentBinding
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.CreateConsentsReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.GroupsTeacher
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.RecordAudioHelper
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import com.iprism.school.viewModels.Scl_ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer

class CreateConsentActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateConsentBinding
    private var selectedValue: String = ""
    private val viewModel: Scl_ViewModel by viewModels()
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private lateinit var crossImage: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseList>()

    private val studentNames = mutableListOf<String>()
    private val studentIds = mutableListOf<String>()
    private val studentList = mutableListOf<StudentList>()

    private val usersNames = mutableListOf<String>()
    private val usersIds = mutableListOf<String>()
    private val usersList = mutableListOf<StaffDetailList>()

    private val groupNames = mutableListOf<String>()
    private val groupIds = mutableListOf<String>()
    private val groupList = mutableListOf<GroupsTeacher>()

    private var selected_class_ids : String? = ""
    private var selected_class_names : String? = ""

    private var selected_student_ids : String? = ""
    private var selected_student_names : String? = ""

    private var selected_users_ids : String? = ""
    private var selected_users_names : String? = ""
    private var selected_group_ids : String? = ""
    private var selected_group_names : String? = ""

    private var tag: String = ""
    lateinit var resultLauncher: ActivityResultLauncher<Uri>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    private var currentPhotoPath: String? = null
    private var videoUri: Uri? = null
    private var audioFilePath: String? = null
    private var mediaRecorder: MediaRecorder? = null

    private var mediaPlayer: MediaPlayer? = null
    private var encodedPic: String? = null
    private var attachment_type: String? = null

    private var timer: Timer? = null
    private var elapsedSeconds = 0

    private var isRecording = false
    private lateinit var recordAudioHelper: RecordAudioHelper
    private val audioList = ArrayList<AudioItem>()
    private lateinit var adapter: AudioAdapter

    private var base64Audio: String? = null

    private var commaSeparatedBase64 : String? = null
    private lateinit var imageAdapter: ImageAdapter
    private val imageUris = mutableListOf<Uri>()
    private lateinit var photoUri: Uri

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tag = intent.getStringExtra("tag").toString()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        setupCheckboxes(binding.checkBox1, binding.checkBox2)
        setDateAndTime()
        handleBack()
        handleTimeLo()
        handleDateLo()
        handleDocumentIv()

        callclasses()

        resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d("cameraCapture", "Photo captured at: $photoUri")
                imageUris.add(photoUri)
                imageAdapter.notifyDataSetChanged()
            } else {
                Log.e("cameraCapture", "Failed to capture photo")
            }
        }

        binding.imagesRv.layoutManager = GridLayoutManager(this,3)
        imageAdapter = ImageAdapter(imageUris) { uri ->
            imageAdapter.deleteImage(uri) }
        binding.imagesRv.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()
        Log.d("images_Uris",imageUris.toString())

//        binding.stopButton.isEnabled = false
//        recordAudioHelper = RecordAudioHelper(this)
//        adapter = AudioAdapter(audioList)
//        binding.imagesRv.layoutManager = LinearLayoutManager(this)
//        binding.imagesRv.adapter = adapter

//        adapter.onDeleteClick = { position ->
//            deleteSingleFile(position)
//        }

        binding.classLl.setOnClickListener {
            showClasses()
        }

        binding.recordButton.setOnClickListener {
            checkPermissionsAndStartRecording()
        }

        binding.stopButton.setOnClickListener {
            stopRecording()
        }

        binding.studentLl.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Class")
            }else{
                showstudents()
            }
        }

        binding.groupsLl.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Class")
            }else if (selected_student_ids == ""||selected_student_ids == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Students")
            } else{
                showGroups()
            }
        }

        binding.createBtn.setOnClickListener(View.OnClickListener {
            if (attachment_type == "audio"){
                convertBase64Audio()
            }

            convertImagesToBase64()

            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Class")
            }else if (selected_student_ids == ""||selected_student_ids == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Students")
            }else if (selected_group_ids == ""||selected_group_ids == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Users")
            }else if (binding.dateTxt.text.toString() == ""||binding.dateTxt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Date")
            }else if (binding.timeTxt.text.toString() == ""||binding.timeTxt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Select Time")
            }else if (binding.detailsTxt.text.toString() == ""||binding.detailsTxt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, "Enter Details")
            }else{

                var apiRequest = CreateConsentsReq(commaSeparatedBase64.toString(),"image",auth_token,
                    selected_class_ids.toString(),binding.dateTxt.text.toString(),binding.detailsTxt.text.toString(),
                    selected_group_ids.toString(),scl_id, selected_student_ids.toString(),teacherId,
                    binding.timeTxt.text.toString(), binding.titleTxt.text.toString(),selectedValue)
                    createConsent(apiRequest)
            }
        })
    }

    private fun createConsent(apiRequest: CreateConsentsReq) {
        showProgress()
        Log.d("createConsentsReq", apiRequest.toString())
        viewModel.createConsents(apiRequest).observe(this@CreateConsentActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                Log.d("createConsentResponse", response.toString())
                startActivity(Intent(this@CreateConsentActivity, ConsentsActivity::class.java))
                finish()
            } else {
                hideProgress()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@CreateConsentActivity, LoginActivity::class.java))
                    finish()
                }
            }
        })

    }

    private fun handleDocumentIv() {
        binding.linkIv.setOnClickListener(View.OnClickListener {
//            showOptionsDialog()
            openGallery()
        })
    }

    private fun setupCheckboxes(checkBox1: CheckBox, checkBox2: CheckBox) {
        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox2.isChecked = false
                selectedValue = "consent"
            } else if (!checkBox2.isChecked) {
                selectedValue = ""
            }
        }

        checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false
                selectedValue = "invitation"
            } else if (!checkBox1.isChecked) {
                selectedValue = ""
            }
        }
    }

    private fun setDateAndTime() {
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val stf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedDate: String = sdfString.format(calendar.time)
        val formattedTime = stf.format(calendar.time)

        binding.dateTxt.text = formattedDate
        binding.timeTxt.text = formattedTime
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, false)
            Log.d("date_Tag", DateTimeUtils.dateMonthYear)
        })
    }

    private fun handleTimeLo() {
        binding.timeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun callclasses() {
        showProgress()
        var loginApiRequest = TeacherAccessReq( teacherId,auth_token )
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<ClassResponse> = parentApiService!!.classes(loginApiRequest)
        call.enqueue(object : Callback<ClassResponse> {
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.classes?.let {
                        hideProgress()
                        classList.clear()
                        classList.addAll(it)
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateConsentActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateConsentActivity, response.message())
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateConsentActivity, t.message.toString())
            }
        })
    }

    private fun callStudents() {
        showProgress()
        var loginApiRequest = CLass_StudentsReq( auth_token,selected_class_ids.toString(),"active",teacherId )
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<Class_studentResponse> = parentApiService!!.teacherStudents(loginApiRequest)
        call.enqueue(object : Callback<Class_studentResponse> {
            override fun onResponse(call: Call<Class_studentResponse>, response: Response<Class_studentResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.students?.let {
                        hideProgress()
                        studentList.clear()
                        studentList.addAll(it)
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateConsentActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateConsentActivity, response.message())
                }
            }
            override fun onFailure(call: Call<Class_studentResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateConsentActivity, t.message.toString())
            }
        })
    }

    private fun callUsers() {
        showProgress()
        var loginApiRequest = SchoolStaffReq( auth_token,scl_id,teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<SchoolStaffResponse> = parentApiService!!.schoolStaff(loginApiRequest)
        call.enqueue(object : Callback<SchoolStaffResponse> {
            override fun onResponse(call: Call<SchoolStaffResponse>, response: Response<SchoolStaffResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.staff_details?.let {
                        hideProgress()
                        usersList.clear()
                        usersList.addAll(it)
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateConsentActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateConsentActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateConsentActivity, t.message.toString())
            }
        })
    }

    private fun callGroups() {
        showProgress()
        var loginApiRequest = SchoolStaffReq( auth_token,scl_id,teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<GroupsResponse> = parentApiService!!.teacherViewGroups(loginApiRequest)
        call.enqueue(object : Callback<GroupsResponse> {
            override fun onResponse(call: Call<GroupsResponse>, response: Response<GroupsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.groups?.let {
                        hideProgress()
                        groupList.clear()
                        groupList.addAll(it)
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()

                    } else {

                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateConsentActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateConsentActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateConsentActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateConsentActivity, t.message.toString())
            }
        })
    }

    private fun showClasses() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Class"

        val originalClassNames = mutableListOf("Select All") + classList.map { it.class_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = classNames.toMutableSet()
        val tempClassIds = classIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        classList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == classList.size) {
            checkedItems[0] = true
            listView.setItemChecked(0, true)
        }

        // Handle ListView item selection
        listView.setOnItemClickListener { _, _, which, _ ->
            if (which == 0) { // "Select All" logic
                val isChecked = !checkedItems[0]
                for (i in 1 until checkedItems.size) {
                    checkedItems[i] = isChecked
                    listView.setItemChecked(i, isChecked)
                }
                if (isChecked) {
                    tempClassNames.clear()
                    tempClassIds.clear()
                    tempClassNames.addAll(classList.map { it.class_name })
                    tempClassIds.addAll(classList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = classList[which - 1].class_name
                val selectedClassId = classList[which - 1].id

                if (tempClassNames.contains(selectedClassName)) {
                    tempClassNames.remove(selectedClassName)
                    tempClassIds.remove(selectedClassId)
                    listView.setItemChecked(which, false)
                } else {
                    tempClassNames.add(selectedClassName)
                    tempClassIds.add(selectedClassId)
                    listView.setItemChecked(which, true)
                }

                // Update "Select All" state
                checkedItems[0] = tempClassNames.size == classList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(classList.map { it.class_name })
                } else {
                    filteredClassNames.addAll(classList.filter { it.class_name.contains(newText, true) }.map { it.class_name })
                }
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                classNames.clear()
                classIds.clear()
                classNames.addAll(tempClassNames)
                classIds.addAll(tempClassIds)

                selected_class_ids = classIds.joinToString(",")
                selected_class_names = classNames.joinToString(" , ")
                binding.selectedclass.text = selected_class_names
                Log.d("SelectedClass", selected_class_ids.toString())

                callStudents()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                classNames.clear()
                classIds.clear()
                selected_class_ids = ""
                selected_class_names = ""
                binding.selectedclass.text = ""

                studentNames.clear()
                studentIds.clear()
                selected_student_names = ""
                selected_student_ids = ""
                binding.selectedstudent.text = ""

                usersIds.clear()
                usersList.clear()
                selected_users_ids = ""
                selected_users_names = ""
            }
            .create()
        dialog.show()
    }

    private fun showstudents() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Students"

        val originalClassNames = mutableListOf("Select All") + studentList.map { it.student_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = studentNames.toMutableSet()
        val tempClassIds = studentIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        studentList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == studentList.size) {
            checkedItems[0] = true
            listView.setItemChecked(0, true)
        }

        // Handle ListView item selection
        listView.setOnItemClickListener { _, _, which, _ ->
            if (which == 0) { // "Select All" logic
                val isChecked = !checkedItems[0]
                for (i in 1 until checkedItems.size) {
                    checkedItems[i] = isChecked
                    listView.setItemChecked(i, isChecked)
                }
                if (isChecked) {
                    tempClassNames.clear()
                    tempClassIds.clear()
                    tempClassNames.addAll(studentList.map { it.student_name })
                    tempClassIds.addAll(studentList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = studentList[which - 1].student_name
                val selectedClassId = studentList[which - 1].id

                if (tempClassNames.contains(selectedClassName)) {
                    tempClassNames.remove(selectedClassName)
                    tempClassIds.remove(selectedClassId)
                    listView.setItemChecked(which, false)
                } else {
                    tempClassNames.add(selectedClassName)
                    tempClassIds.add(selectedClassId)
                    listView.setItemChecked(which, true)
                }

                // Update "Select All" state
                checkedItems[0] = tempClassNames.size == studentList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(studentList.map { it.student_name })
                } else {
                    filteredClassNames.addAll(studentList.filter { it.student_name.contains(newText, true) }.map { it.student_name })
                }
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                studentNames.clear()
                studentIds.clear()
                studentNames.addAll(tempClassNames)
                studentIds.addAll(tempClassIds)

                selected_student_ids = studentIds.joinToString(",")
                selected_student_names = studentNames.joinToString(" , ")
                binding.selectedstudent.text = selected_student_names
                Log.d("selectedStudents", selected_student_ids.toString())
                callGroups()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                studentNames.clear()
                studentIds.clear()
                selected_student_names = ""
                selected_student_ids = ""
                binding.selectedstudent.text = ""

                usersIds.clear()
                usersList.clear()
                selected_users_ids = ""
                selected_users_names = ""
            }
            .create()
        dialog.show()
    }

    private fun showusers() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select User"

        val originalClassNames = mutableListOf("Select All") + usersList.map { it.employee_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = usersNames.toMutableSet()
        val tempClassIds = usersIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        usersList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == usersList.size) {
            checkedItems[0] = true
            listView.setItemChecked(0, true)
        }

        // Handle ListView item selection
        listView.setOnItemClickListener { _, _, which, _ ->
            if (which == 0) { // "Select All" logic
                val isChecked = !checkedItems[0]
                for (i in 1 until checkedItems.size) {
                    checkedItems[i] = isChecked
                    listView.setItemChecked(i, isChecked)
                }
                if (isChecked) {
                    tempClassNames.clear()
                    tempClassIds.clear()
                    tempClassNames.addAll(usersList.map { it.employee_name })
                    tempClassIds.addAll(usersList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = usersList[which - 1].employee_name
                val selectedClassId = usersList[which - 1].id

                if (tempClassNames.contains(selectedClassName)) {
                    tempClassNames.remove(selectedClassName)
                    tempClassIds.remove(selectedClassId)
                    listView.setItemChecked(which, false)
                } else {
                    tempClassNames.add(selectedClassName)
                    tempClassIds.add(selectedClassId)
                    listView.setItemChecked(which, true)
                }

                // Update "Select All" state
                checkedItems[0] = tempClassNames.size == usersList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(usersList.map { it.employee_name })
                } else {
                    filteredClassNames.addAll(usersList.filter { it.employee_name.contains(newText, true) }.map { it.employee_name })
                }
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                usersNames.clear()
                usersIds.clear()
                usersNames.addAll(tempClassNames)
                usersIds.addAll(tempClassIds)

                selected_users_ids = usersIds.joinToString(",")
                selected_users_names = usersNames.joinToString(" , ")
                binding.selectedusers.text = selected_users_names
                Log.d("selectedUsers", selected_users_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                usersIds.clear()
                usersList.clear()
                selected_users_ids = ""
                selected_users_names = ""
            }
            .create()
        dialog.show()
    }

    private fun showGroups() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Groups"

        val originalClassNames = mutableListOf("Select All") + groupList.map { it.group_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = groupNames.toMutableSet()
        val tempClassIds = groupIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        groupList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == groupList.size) {
            checkedItems[0] = true
            listView.setItemChecked(0, true)
        }

        // Handle ListView item selection
        listView.setOnItemClickListener { _, _, which, _ ->
            if (which == 0) { // "Select All" logic
                val isChecked = !checkedItems[0]
                for (i in 1 until checkedItems.size) {
                    checkedItems[i] = isChecked
                    listView.setItemChecked(i, isChecked)
                }
                if (isChecked) {
                    tempClassNames.clear()
                    tempClassIds.clear()
                    tempClassNames.addAll(groupList.map { it.group_name })
                    tempClassIds.addAll(groupList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = groupList[which - 1].group_name
                val selectedClassId = groupList[which - 1].id

                if (tempClassNames.contains(selectedClassName)) {
                    tempClassNames.remove(selectedClassName)
                    tempClassIds.remove(selectedClassId)
                    listView.setItemChecked(which, false)
                } else {
                    tempClassNames.add(selectedClassName)
                    tempClassIds.add(selectedClassId)
                    listView.setItemChecked(which, true)
                }

                // Update "Select All" state
                checkedItems[0] = tempClassNames.size == groupList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(groupList.map { it.group_name })
                } else {
                    filteredClassNames.addAll(groupList.filter { it.group_name.contains(newText, true) }.map { it.group_name })
                }
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                groupNames.clear()
                groupIds.clear()
                groupNames.addAll(tempClassNames)
                groupIds.addAll(tempClassIds)

                selected_group_ids = groupIds.joinToString(",")
                selected_group_names = groupNames.joinToString(" , ")
                binding.selectedgroups.text = selected_group_names
                Log.d("selectedgroups", selected_group_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                groupIds.clear()
                groupList.clear()
                selected_group_ids = ""
                selected_group_names = ""
                binding.selectedgroups.text =""
            }
            .create()
        dialog.show()
    }

    private fun showOptionsDialog() {
        encodedPic = ""

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_media_selection, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        // Get views
        val cameraImageLayout = dialogView.findViewById<LinearLayout>(R.id.cameraImageLayout)
        val cameraRecordingLayout = dialogView.findViewById<LinearLayout>(R.id.cameraRecordingLayout)
        val audioRecordingLayout = dialogView.findViewById<LinearLayout>(R.id.audioRecordingLayout)

        // Handle clicks
        cameraImageLayout.setOnClickListener {
            // Open Camera for Image
//            openCameraForImage()
            attachment_type = "image"
//            selectImage()
            openGallery()
            dialog.dismiss()
        }

        cameraRecordingLayout.setOnClickListener {
            // Open Camera for Video Recording
            attachment_type = "video"
//            openCameraForVideo()
            dialog.dismiss()
        }

        audioRecordingLayout.setOnClickListener {
            // Start Audio Recording
            attachment_type = "audio"
            binding.llAudio.visibility = View.VISIBLE
//            checkPermissionsAndStartRecording()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkPermissionsAndStartRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED) {
            startRecording()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startRecording() {
        if (isRecording) {
            Toast.makeText(this, "Already recording", Toast.LENGTH_SHORT).show()
            return
        }
        recordAudioHelper.startRecording()?.let { audioPath ->
            isRecording = true
            binding.  recordButton.isEnabled = false
            binding.stopButton.isEnabled = true

            // Start the chronometer and show recording indicator layout
            binding.recordingInfoLayout.visibility = LinearLayout.VISIBLE
            binding.chronometerRecording.base = SystemClock.elapsedRealtime()
            binding.chronometerRecording.start()

            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        if (!isRecording) {
            Toast.makeText(this, "Recording not in progress", Toast.LENGTH_SHORT).show()
            return
        }
        recordAudioHelper.stopRecording()

        // Stop and hide the chronometer and indicator
        binding.chronometerRecording.stop()
       binding. recordingInfoLayout.visibility = LinearLayout.GONE

        recordAudioHelper.audioFilePath?.let { audioPath ->
            audioList.add(AudioItem(audioPath))
            adapter.notifyDataSetChanged()
        }
        isRecording = false
       binding. recordButton.isEnabled = true
        binding.stopButton.isEnabled = false

//        convertBase64Audio()

        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
    }

    private fun deleteSingleFile(position: Int) {
        if (position < 0 || position >= audioList.size) return
        val audioItem = audioList[position]
        val file = File(audioItem.path)
        if (file.exists() && file.delete()) {
            audioList.removeAt(position)
            adapter.notifyItemRemoved(position)
            Toast.makeText(this, "Recording deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to delete recording", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertBase64Audio() {
        val selectedItems = audioList.filter { it.isSelected }

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "No recordings selected.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("SelectedItems", "Paths: ${selectedItems.map { it.path }}")

        val base64Map = mutableMapOf<String, String>()

        selectedItems.forEach { audioItem ->
            val file = File(audioItem.path)
            if (!file.exists()) {
                Log.e("Base64Error", "File not found: ${audioItem.path}")
                return@forEach
            }

            try {
                val inputStream = file.inputStream()
                val bytes = inputStream.readBytes()
                inputStream.close()

                val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                val fileName = file.name

                base64Map[fileName] = base64String
                Log.d("Base64Success", "File: $fileName, Base64 (first 100 chars): ${base64String.take(100)}")
//                Log.d("Base64Success", base64String.toString())
            } catch (e: Exception) {
                Log.e("Base64Error", "Error encoding ${audioItem.path}: ${e.message}")
            }
        }

//        Log.d("consent_Audio_Base64", base64Audio)
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkPermissionsAndStartRecording()
        } else {
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        adapter.releasePlayer()
//    }

    private fun convertImagesToBase64() {
        val base64Strings = imageUris.mapNotNull { uri -> uriToBase64(this@CreateConsentActivity, uri) }
        commaSeparatedBase64 = base64Strings.joinToString(",")
        Log.d("base64String", commaSeparatedBase64.toString())
    }

    private fun uriToBase64(context: CreateConsentActivity, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Create a URI where the camera image should be saved.
    private fun createImageUri(): Uri {
        // Create a unique file name in the app's external pictures directory.
        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "camera_image_${System.currentTimeMillis()}.jpg")
        // Get URI using FileProvider.
        return FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this@CreateConsentActivity,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this@CreateConsentActivity)
            if (items[item] == "Take Photo") {
                // userChoosenTask = "Take Photo"
                openCamera()
            } else if (items[item] == "Choose from Gallery") {
                //userChoosenTask = "Choose from Gallery"
                openGallery()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this?.let {
                intent.resolveActivity(this.packageManager)?.also {
                    photoUri = createImageUri()
                    resultLauncher.launch(photoUri)
//                    resultLauncher.launch(intent)
//                    pickImageLauncher.launch(intent)
                }
            }
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            this?.let {
                intent.resolveActivity(this.packageManager)?.also {
                    pickImageLauncher.launch(intent)
                }
            }
        }
    }


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data?.data != null) {
            val imageUri = result.data!!.data!!
            imageUris.add(imageUri)
            imageAdapter.notifyDataSetChanged()

        }
    }

}