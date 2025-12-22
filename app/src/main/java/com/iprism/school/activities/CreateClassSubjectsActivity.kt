package com.iprism.school.activities

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
import com.iprism.school.databinding.ActivityCreateClassSubjectsBinding
import com.iprism.school.model.Request.AddSubjectTeacherReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.SubjectLsit
import com.iprism.school.model.Response.SubjectsListResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateClassSubjectsActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateClassSubjectsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private val subjectNames = mutableListOf<String>()
    private val subjectIds = mutableListOf<String>()
    private val subjectList = mutableListOf<SubjectLsit>()

    private val teacherNames = mutableListOf<String>()
    private val teacherIds = mutableListOf<String>()
    private val teacherList = mutableListOf<StaffDetailList>()

    private var selected_subject_ids : String? = ""
    private var selected_subject_names : String? = ""

    private var selected_teacher_ids : String? = ""
    private var selected_teacher_names : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleBack()
        handleSaveBtn()
        callSubjects()

        binding.subjectLo.setOnClickListener {
            showsubjects()
        }

        binding.teachersLo.setOnClickListener {
            showteacher()
        }
    }

    private fun handleSaveBtn() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            if (selected_subject_ids == ""||selected_subject_ids == null){
                showToast("Select Subject")
            }else if (selected_teacher_ids == ""||selected_teacher_ids == null){
                showToast("Select Teacher")
            }else{
                create()
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun callSubjects() {
        showProgress()
        var loginApiRequest = SchoolStaffReq( auth_token,scl_id,teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<SubjectsListResponse> = parentApiService!!.subjectsList(loginApiRequest)
        call.enqueue(object : Callback<SubjectsListResponse> {
            override fun onResponse(call: Call<SubjectsListResponse>, response: Response<SubjectsListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.subjects?.let {
                        hideProgress()
                        subjectList.clear()
                        subjectList.addAll(it)
                        callTeachers()
                    }

                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                        callTeachers()
                    } else {
                        callTeachers()
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateClassSubjectsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateClassSubjectsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassSubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SubjectsListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassSubjectsActivity, t.message.toString())
            }
        })
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
                        ToastUtils.showSuccessCustomToast(this@CreateClassSubjectsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateClassSubjectsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassSubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassSubjectsActivity, t.message.toString())
            }
        })
    }

    private fun showsubjects() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Choose Admin"

        val originalClassNames = subjectList.map { it.subject_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, originalClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Restore previously selected admin
        val selectedIndex = subjectList.indexOfFirst { it.id == selected_subject_ids }
        if (selectedIndex != -1) {
            listView.setItemChecked(selectedIndex, true)
        }

        var selectedAdminName = selected_subject_names
        var selectedAdminId = selected_subject_ids

        // Handle selection
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedAdminName = subjectList[position].subject_name
            selectedAdminId = subjectList[position].id
        }

        // Build and Show AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                selected_subject_names = selectedAdminName
                selected_subject_ids = selectedAdminId
                binding.subjectTxt.text = selected_subject_names
//                Log.d("selectedAdmin", "ID: $selected_admin_ids, Name: $selected_admin_names")
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun showteacher() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Choose "

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
                binding.teacherTxt.text = selected_teacher_names
//                Log.d("selectedAdmin", "ID: $selected_admin_ids, Name: $selected_admin_names")
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun create() {
        showProgress()
        var apiRequest = AddSubjectTeacherReq(auth_token,scl_id,selected_subject_ids.toString(),teacherId,selected_teacher_ids.toString())
        Log.d("addSubject", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.addTeacherSubject(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@CreateClassSubjectsActivity, ClassSubjectsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateClassSubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateClassSubjectsActivity, t.message.toString())
            }
        })
    }


}