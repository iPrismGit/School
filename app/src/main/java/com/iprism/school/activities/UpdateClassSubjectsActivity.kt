package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.CreateClassSubjectsActivity
import com.iprism.school.activities.classes.CreateClassActivity
import com.iprism.school.databinding.ActivityCreateClassSubjectsBinding
import com.iprism.school.databinding.ActivityUpDateClassSubjectsBinding
import com.iprism.school.databinding.SchoolMembersBottomSheetBinding
import com.iprism.school.model.Request.AddSubjectTeacherReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherSubjectReq
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateClassSubjectsActivity : BaseActivity() {

    private lateinit var binding: ActivityUpDateClassSubjectsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var subject_Id: String = ""
    private var subName: String = ""
    private var teachName: String = ""

    private val teacherNames = mutableListOf<String>()
    private val teacherIds = mutableListOf<String>()
    private val teacherList = mutableListOf<StaffDetailList>()

    private var selected_teacher_ids : String? = ""
    private var selected_teacher_names : String? = ""

//    intent.putExtra("subject_Id",subject_Id)
//    intent.putExtra("teachId",teachId)
//    intent.putExtra("subName",subName)
//    intent.putExtra("teachName",teachName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpDateClassSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        subject_Id = intent.getStringExtra("subject_Id").toString()
        selected_teacher_ids = intent.getStringExtra("teachId").toString()
        subName = intent.getStringExtra("subName").toString()
        teachName = intent.getStringExtra("teachName").toString()

        binding.selectedteacherTv.text = teachName
        binding.subjectName.text =  "Subject Name : "+ "(" +subName+" )"

        handleBack()
        handleSave()
        handleTeachersLo()

        callTeachers()


    }

    private fun handleTeachersLo() {
        binding.teachersLl.setOnClickListener(View.OnClickListener {
            showteacher()
        })
    }

    private fun handleSave() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            if (selected_teacher_ids == ""||selected_teacher_ids == null){
                showToast("Select teacher")
            }else{
                updateTeacherSubjects()
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showTeachersBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val teachersBinding = SchoolMembersBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(teachersBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            teachersBinding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Added Teachers")
            })

            teachersBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            teachersBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
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
                        ToastUtils.showSuccessCustomToast(this@UpdateClassSubjectsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@UpdateClassSubjectsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@UpdateClassSubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@UpdateClassSubjectsActivity, t.message.toString())
            }
        })
    }

    private fun showteacher() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Choose"

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
                binding.selectedteacherTv.text = selected_teacher_names.toString()
//                Log.d("selectedAdmin", "ID: $selected_admin_ids, Name: $selected_admin_names")
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun updateTeacherSubjects() {
        showProgress()
        var apiRequest = TeacherSubjectReq(auth_token,scl_id,subject_Id,teacherId)
        Log.d("addSubject", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.updateTeacherSubject(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@UpdateClassSubjectsActivity, ClassSubjectsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@UpdateClassSubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@UpdateClassSubjectsActivity, t.message.toString())
            }
        })
    }

}