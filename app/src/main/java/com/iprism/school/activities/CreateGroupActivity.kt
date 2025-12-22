package com.iprism.school.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateGroupBinding
import com.iprism.school.model.Request.CreateGroupReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.StudentsListReq
import com.iprism.school.model.Request.UpdateGroupReq
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.StudentListResponse
import com.iprism.school.model.Response.StudentListt
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64

class CreateGroupActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateGroupBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var student_Type: String = "active"

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""

    private val studentNames = mutableListOf<String>()
    private val studentIds = mutableListOf<String>()
    private val studentList = mutableListOf<StudentListt>()

    private val usersNames = mutableListOf<String>()
    private val usersIds = mutableListOf<String>()
    private val usersList = mutableListOf<StaffDetailList>()

    private val adminNames = mutableListOf<String>()
    private val adminIds = mutableListOf<String>()
    private val adminList = mutableListOf<StaffDetailList>()

    private var selected_student_ids : String? = ""
    private var selected_student_names : String? = ""

    private var selected_users_ids : String? = ""
    private var selected_users_names : String? = ""

    private var selected_admin_ids : String? = ""
    private var selected_admin_names : String? = ""

    private var type : String? = ""
    private var groupId : String? = ""
    private var groupName : String? = ""
    private var groupDescription : String? = ""
    private var admins : String? = ""
    private var adminsId : String? = ""
    private var schoolmem : String? = ""
    private var schoolmemId : String? = ""
    private var students : String? = ""
    private var studentsId : String? = ""
    private var groupImg : String? = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()

        if (tag == "edit"){

            groupImg = intent.getStringExtra("groupImg").toString()


            Glide.with(this@CreateGroupActivity)
                .load(Constants.IMAGES_URL+groupImg)
                .into(binding.picImg)

            groupId = intent.getStringExtra("groupId").toString()
            groupName = intent.getStringExtra("groupName").toString()
            groupDescription = intent.getStringExtra("groupDescription").toString()
            admins = intent.getStringExtra("admins").toString()
            adminsId = intent.getStringExtra("adminsId").toString()
            schoolmem = intent.getStringExtra("schoolmem").toString()
            schoolmemId = intent.getStringExtra("schoolmemId").toString()
            students = intent.getStringExtra("students").toString()
            studentsId = intent.getStringExtra("studentsId").toString()

            binding.nameEt.setText(groupName)
            binding.descriptionEt.setText(groupDescription)
            binding.selectedadmin.setText(admins)
            binding.selectedschoolmem.setText(schoolmem)
            binding.selectedstudent.setText(students)

            selected_admin_ids = adminsId.toString()
            selected_users_ids = schoolmemId.toString()
            selected_student_ids = studentsId.toString()

            binding.createBtn.text = "Update"
            binding.titleTv.text = "Update Group"

        }else{
            binding.createBtn.text = "Create"
            binding.titleTv.text = "Create Group"
        }

        handleBack()
        handleCreateBtn()
        callAdmin()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val bitmap = data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val b = baos.toByteArray()
                val encoder: Base64.Encoder = Base64.getEncoder()
                binding.picImg.setImageBitmap(bitmap)
                encodedPic = encoder.encodeToString(b)
            }
        }

        resultLaunchergallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val uri = data?.data
                val imagestrem : InputStream? = contentResolver?.openInputStream(uri!!)
                val selectedImage  : Bitmap = BitmapFactory.decodeStream(imagestrem)
                binding.picImg.setImageBitmap(selectedImage)
                encodedPic = encodeImage(selectedImage)
            }
        }
        binding.picImg.setOnClickListener {
            selectImage()
        }
        binding.adminLl.setOnClickListener {
            showadmin()
        }
        binding.schoolmemLl.setOnClickListener {
            showusers()
        }
        binding.studentLl.setOnClickListener {
            showstudents()
        }
    }

    private fun handleCreateBtn() {
        binding.createBtn.setOnClickListener(View.OnClickListener {
            if (tag == "edit"){
                if (binding.nameEt.text.toString()== ""||binding.nameEt.text.toString()== null){
                    showToast("Enter Group Name")
                }else if (binding.descriptionEt.text.toString()== ""||binding.descriptionEt.text.toString()==null){
                    showToast("Enter Description")
                }else if (selected_admin_ids== ""||selected_admin_ids== null){
                    showToast("Select Admin")
                }else if (selected_users_ids== ""||selected_users_ids== null){
                    showToast("Select School Members")
                }else if (selected_student_ids== ""||selected_student_ids== null){
                    showToast("Select Students")
                }else{
                    updateGroup()
                }

            }else{

                if (encodedPic == ""||encodedPic == null){
                    showToast("Select Pic")
                }else if (binding.nameEt.text.toString()== ""||binding.nameEt.text.toString()== null){
                    showToast("Enter Group Name")
                }else if (binding.descriptionEt.text.toString()== ""||binding.descriptionEt.text.toString()==null){
                    showToast("Enter Description")
                }else if (selected_admin_ids== ""||selected_admin_ids== null){
                    showToast("Select Admin")
                }else if (selected_users_ids== ""||selected_users_ids== null){
                    showToast("Select School Members")
                }else if (selected_student_ids== ""||selected_student_ids== null){
                    showToast("Select Students")
                }else{
                    createGroup()
                }
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun callStudentsList() {
        showProgress()
        var apiRequest = StudentsListReq(auth_token,"",scl_id,"",student_Type,teacherId,type.toString())
        Log.d("student_List", apiRequest.toString())
        val call: Call<StudentListResponse> = parentApiService!!.studentsList(apiRequest)
        call.enqueue(object : Callback<StudentListResponse> {
            override fun onResponse(call: Call<StudentListResponse>, response: Response<StudentListResponse>) {
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
                        ToastUtils.showSuccessCustomToast(this@CreateGroupActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateGroupActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }

                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateGroupActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateGroupActivity, t.message.toString())
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
                        callStudentsList()
                    }

                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                        callStudentsList()
                    } else {
                        callStudentsList()
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateGroupActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateGroupActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateGroupActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateGroupActivity, t.message.toString())
            }
        })
    }

    private fun callAdmin() {
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
                        adminList.clear()
                        adminList.addAll(it)

                        callUsers()
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        callUsers()
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateGroupActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateGroupActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateGroupActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateGroupActivity, t.message.toString())
            }
        })
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

    private fun showadmin() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Choose Admin"

        val originalClassNames = mutableListOf("Select All") + adminList.map { it.employee_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = adminNames.toMutableSet()
        val tempClassIds = adminIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        adminList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == adminList.size) {
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
                    tempClassNames.addAll(adminList.map { it.employee_name })
                    tempClassIds.addAll(adminList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = adminList[which - 1].employee_name
                val selectedClassId = adminList[which - 1].id

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
                checkedItems[0] = tempClassNames.size == adminList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(adminList.map { it.employee_name })
                } else {
                    filteredClassNames.addAll(adminList.filter { it.employee_name.contains(newText, true) }.map { it.employee_name })
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
                adminNames.clear()
                adminIds.clear()
                adminNames.addAll(tempClassNames)
                adminIds.addAll(tempClassIds)

                selected_admin_ids = adminIds.joinToString(",")
                selected_admin_names = adminNames.joinToString(",")
                binding.selectedadmin.text = selected_admin_names.toString()
                Log.d("selectedUsers", selected_admin_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                selected_admin_ids = ""
                selected_admin_names = ""
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
                binding.selectedschoolmem.text = selected_users_names
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


    private fun createGroup() {
        showProgress()
        var apiRequest = CreateGroupReq(encodedPic.toString(),auth_token,selected_admin_ids.toString()
            ,binding.descriptionEt.text.toString(),binding.nameEt.text.toString(),selected_users_ids.toString(),
            selected_student_ids.toString(),scl_id,teacherId)
        Log.d("createGroupReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.createGroup(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@CreateGroupActivity, GroupsActivity::class.java)
                        startActivity(intent)
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateGroupActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateGroupActivity, t.message.toString())
            }
        })
    }


    private fun updateGroup() {
        showProgress()
        var apiRequest = UpdateGroupReq(encodedPic.toString(),auth_token,selected_admin_ids.toString()
            ,binding.descriptionEt.text.toString(),groupId.toString(),binding.nameEt.text.toString(),selected_users_ids.toString(),
            selected_student_ids.toString(),scl_id,teacherId)
        Log.d("createGroupReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.groupUpdate(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@CreateGroupActivity, GroupsActivity::class.java)
                        startActivity(intent)
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateGroupActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateGroupActivity, t.message.toString())
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage(selectedImage: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val b = baos.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        encodedPic = encoder.encodeToString(b)

        return encodedPic
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this)
            if (items[item] == "Take Photo") {
                // userChoosenTask = "Take Photo"
                openCamera()
            } else if (items[item] == "Choose from Gallery") {
                //userChoosenTask = "Choose from Gallery"
                openGalleryy()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openGalleryy() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            resultLaunchergallery.launch(intent)
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this.let {
                intent.resolveActivity(it.packageManager)?.also {
                    resultLauncher.launch(intent)
                }
            }
        }
    }

}