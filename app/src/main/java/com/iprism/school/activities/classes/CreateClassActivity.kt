package com.iprism.school.activities.classes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.ClassesActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.databinding.ActivityCreateClassBinding
import com.iprism.school.model.Request.ClassDetailsReq
import com.iprism.school.model.Request.ClassStatusChangeReq
import com.iprism.school.model.Request.CreateClassReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.UpdateClassReq
import com.iprism.school.model.Response.ClassDetailsResponse
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.SessionList
import com.iprism.school.model.Response.SessionListResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateClassActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateClassBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private val sessionNames = mutableListOf<String>()
    private val sessionIds = mutableListOf<String>()
    private val sessionList = mutableListOf<SessionList>()

    private val teacherNames = mutableListOf<String>()
    private val teacherIds = mutableListOf<String>()
    private val teacherList = mutableListOf<StaffDetailList>()

    private var selected_session_ids : String? = ""
    private var selected_session_names : String? = ""

    private var selected_teacher_ids : String? = ""
    private var selected_teacher_names : String? = ""

    private var classId : String? = ""
    private var class_name : String? = ""
    private var class_section : String? = ""
    private var class_session : String? = ""

    private var class_status : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()

        if (tag == "edit"){

            classId = intent.getStringExtra("classId").toString()
            binding.titleTv.text = "Edit Class".toString()
            binding.submitBtn.text = "Update Class".toString()

            detailsClass()

        }else {
            callSessions()
        }



        binding.crossIv.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.sessionLl.setOnClickListener {
            showSession()
        }

        binding.teacherll.setOnClickListener {
            showTeacher()
        }

        binding.submitBtn.setOnClickListener {
            if (tag == "edit"){
                if (binding.classNameEt.text.toString()== ""||binding.classNameEt.text.toString()== null){
                    showToast("Enter Class Name")
                }else if (binding.sectionNameEt.text.toString()== ""||binding.sectionNameEt.text.toString()== null){
                    showToast("Enter Section Name")
                }else if (selected_session_ids == ""||selected_session_ids == null){
                    showToast("Select Session")
                }else if (selected_teacher_ids == ""||selected_teacher_ids == null){
                    showToast("Select Teacher")
                }else{
                    callUpdateClass()
                }
            }else{
                if (binding.classNameEt.text.toString()== ""||binding.classNameEt.text.toString()== null){
                    showToast("Enter Class Name")
                }else if (binding.sectionNameEt.text.toString()== ""||binding.sectionNameEt.text.toString()== null){
                    showToast("Enter Section Name")
                }else if (selected_session_ids == ""||selected_session_ids == null){
                    showToast("Select Session")
                }else if (selected_teacher_ids == ""||selected_teacher_ids == null){
                    showToast("Select Teacher")
                }else{
                    callCreateClass()
                }
            }
        }
    }

    private fun detailsClass() {
        showProgress()
        var apiRequest = ClassDetailsReq(auth_token,classId.toString(),scl_id.toString(),teacherId)
        Log.d("details_Class", apiRequest.toString())
        val call: Call<ClassDetailsResponse> = parentApiService!!.classDetails(apiRequest)
        call.enqueue(object : Callback<ClassDetailsResponse> {
            override fun onResponse(call: Call<ClassDetailsResponse>, response: Response<ClassDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    callSessions()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        class_status = loginApiResponse.response.subjects[0].status.toString()

                        if (class_status == "1"){
                            binding.activeBtn.visibility = View.GONE
                            binding.deactiveBtn.visibility = View.VISIBLE
                        }else{
                            binding.activeBtn.visibility = View.VISIBLE
                            binding.deactiveBtn.visibility = View.GONE
                        }

                        binding.classNameEt.setText(loginApiResponse.response.subjects[0].class_name)
                        binding.sectionNameEt.setText(loginApiResponse.response.subjects[0].class_section)
                        binding.selectedSessionTv.text = loginApiResponse.response.subjects[0].class_session.toString()
                        binding.selectedTeacherTv.text = loginApiResponse.response.subjects[0].teacher_names.toString()

                        selected_session_ids = loginApiResponse.response.subjects[0].session_id.toString()
                        selected_teacher_ids = loginApiResponse.response.subjects[0].teacher_ids.toString()

                        binding.activeBtn.setOnClickListener {
                            val status = "activate"
                            classchangeStatus(status)
                        }

                        binding.deactiveBtn.setOnClickListener {
                            val status = "deactivate"
                            classchangeStatus(status)
                        }

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassActivity, response.message())
                }
            }
            override fun onFailure(call: Call<ClassDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassActivity, t.message.toString())
            }
        })
    }

    private fun callUpdateClass() {
        showProgress()
        var apiRequest = UpdateClassReq(auth_token,classId.toString(),binding.classNameEt.text.toString()
            ,binding.sectionNameEt.text.toString(),selected_teacher_ids.toString(),scl_id.toString(),
            selected_session_ids.toString(),teacherId)
        Log.d("create_Class", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.updateClass(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@CreateClassActivity, ClassesActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassActivity, t.message.toString())
            }
        })
    }

    private fun callCreateClass() {
        showProgress()
        var apiRequest = CreateClassReq(auth_token,binding.classNameEt.text.toString()
            ,binding.sectionNameEt.text.toString(),selected_teacher_ids.toString(),scl_id,selected_session_ids.toString(),teacherId)
        Log.d("create_Class", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.createClass(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@CreateClassActivity, ClassesActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassActivity, t.message.toString())
            }
        })
    }

    private fun classchangeStatus(status: String) {
        showProgress()
        var apiRequest = ClassStatusChangeReq(auth_token,classId.toString(),scl_id,teacherId,status)
        val call: Call<SuccessResponsePojo> = parentApiService!!.statusClass(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@CreateClassActivity, ClassesActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassActivity, t.message.toString())
            }
        })
    }


    private fun callSessions() {
        showProgress()
        var loginApiRequest = SchoolStaffReq( auth_token,scl_id,teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<SessionListResponse> = parentApiService!!.sessionList(loginApiRequest)
        call.enqueue(object : Callback<SessionListResponse> {
            override fun onResponse(call: Call<SessionListResponse>, response: Response<SessionListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.sessions?.let {
                        hideProgress()
                        sessionList.clear()
                        sessionList.addAll(it)
                        callTeachers()
                    }
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                        callTeachers()
                    } else {
                        callTeachers()
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateClassActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateClassActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SessionListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassActivity, t.message.toString())
            }
        })
    }

    private fun showSession() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Session"

        val originalClassNames = sessionList.map { it.session_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, originalClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Restore previously selected admin
        val selectedIndex = sessionList.indexOfFirst { it.id == selected_session_ids}
        if (selectedIndex != -1) {
            listView.setItemChecked(selectedIndex, true)
        }

        var selectedAdminName = selected_teacher_names
        var selectedAdminId = selected_teacher_ids

        // Handle selection
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedAdminName = sessionList[position].session_name.toString()
            selectedAdminId = sessionList[position].id.toString()
        }

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                selected_session_names = selectedAdminName
                selected_session_ids = selectedAdminId
                binding.selectedSessionTv.text = selected_session_names
//                Log.d("selectedAdmin", "ID: $selected_admin_ids, Name: $selected_admin_names")
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun callTeachers() {
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
                        teacherList.clear()
                        teacherList.addAll(it)
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateClassActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateClassActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassActivity, t.message.toString())
            }
        })
    }

    private fun showTeacher() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Teacher "

        val originalClassNames = teacherList.map { it.employee_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, originalClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Restore previously selected admin
        val selectedIndex = teacherList.indexOfFirst { it.id == selected_teacher_ids }
        if (selectedIndex != -1) {
            listView.setItemChecked(selectedIndex, true)
        }

        var selectedAdminName = selected_teacher_names
        var selectedAdminId = selected_teacher_ids

        // Handle selection
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedAdminName = teacherList[position].employee_name
            selectedAdminId = teacherList[position].id
        }

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                selected_teacher_names = selectedAdminName
                selected_teacher_ids = selectedAdminId
                binding.selectedTeacherTv.text = selected_teacher_names
//                Log.d("selectedAdmin", "ID: $selected_admin_ids, Name: $selected_admin_names")
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }


}
