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
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.ClassesActivity
import com.iprism.school.activities.CreateStaffActivity
import com.iprism.school.activities.classes.CreateClassActivity
import com.iprism.school.adapters.ClassSubjectsAdapter
import com.iprism.school.adapters.ClassesAdapter
import com.iprism.school.databinding.ActivityClassSubjectsBinding
import com.iprism.school.databinding.ActivitySubjectsBinding
import com.iprism.school.databinding.ClassSubjectItemBinding
import com.iprism.school.interfaces.OnSubjectClickListener
import com.iprism.school.model.Request.AllclassesReq
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.ClassListReq
import com.iprism.school.model.Request.SUbjectsTeacherListReq
import com.iprism.school.model.Response.AllClassesResponse
import com.iprism.school.model.Response.ClassListResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ClasseAllList
import com.iprism.school.model.Response.SubjectTeacherListResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassSubjectsActivity : BaseActivity() {

    private lateinit var binding: ActivityClassSubjectsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var type: String = "active"

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseAllList>()

    private var selected_class_ids : String? = ""
    private var selected_class_names : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleBack()
        handleAddBtn()

        callclasses()

//        callClassSubjectList()


        binding.classLl.setOnClickListener {
            showClasses()
        }

    }

    private fun handleAddBtn() {
        binding.addClassSubjectsBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateClassSubjectsActivity::class.java))
        })
    }

//    private fun setupClassSubjectsAdapter() {
//        var classSubjectsAdapter = ClassSubjectsAdapter(this)
//        binding.classSubjectsRv.adapter = classSubjectsAdapter
//        var linearLayoutManager = LinearLayoutManager(this)
//        binding.classSubjectsRv.layoutManager = linearLayoutManager
//        classSubjectsAdapter.setListener(object : OnSubjectClickListener {
//            override fun onItemClick(id: String) {
//                var intent =
//                    Intent(this@ClassSubjectsActivity, UpdateClassSubjectsActivity::class.java)
//                startActivity(intent)
//            }
//        })
//    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun callClassSubjectList() {
        showProgress()
        var apiRequest = SUbjectsTeacherListReq(auth_token,selected_class_ids.toString(),scl_id,teacherId)
        Log.d("class_ListReq", apiRequest.toString())
        val call: Call<SubjectTeacherListResponse> = parentApiService!!.teacherSubjectList(apiRequest)
        call.enqueue(object : Callback<SubjectTeacherListResponse> {
            override fun onResponse(call: Call<SubjectTeacherListResponse>, response: Response<SubjectTeacherListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.nodata.visibility = View.GONE
                        binding.classSubjectsRv.visibility = View.VISIBLE

                        val adap1 = ClassSubjectsAdapter(this@ClassSubjectsActivity, loginApiResponse.response.subjects)
                        binding.classSubjectsRv.layoutManager = LinearLayoutManager(this@ClassSubjectsActivity, LinearLayoutManager.VERTICAL, false)
                        binding.classSubjectsRv.adapter = adap1
                        adap1.notifyDataSetChanged()

                        adap1.OnItemCallBack = {
                                mydata ->
                            val subject_Id = mydata.id.toString()
                            val teachId = mydata.teacher_ids.toString()
                            val subName = mydata.subject_name.toString()
                            val teachName = mydata.teachers.toString()

                            val intent = Intent(this@ClassSubjectsActivity, UpdateClassSubjectsActivity::class.java)
                            intent.putExtra("subject_Id",subject_Id)
                            intent.putExtra("teachId",teachId)
                            intent.putExtra("subName",subName)
                            intent.putExtra("teachName",teachName)
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }
                    }else{
//                        binding.nodata.visibility = View.VISIBLE
//                        binding.rvList.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@ClassSubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SubjectTeacherListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@ClassSubjectsActivity, t.message.toString())
            }
        })
    }


    private fun callclasses() {
        showProgress()
        var loginApiRequest = AllclassesReq(auth_token,"","",scl_id,"",teacherId,"active")
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<AllClassesResponse> = parentApiService!!.allClasses(loginApiRequest)
        call.enqueue(object : Callback<AllClassesResponse> {
            override fun onResponse(call: Call<AllClassesResponse>, response: Response<AllClassesResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.classes?.let {
                        hideProgress()
                        classList.clear()
                        classList.addAll(it)
                        callClassSubjectList()
                    }
                    hideProgress()
                    callClassSubjectList()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@ClassSubjectsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@ClassSubjectsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    callClassSubjectList()
                    ToastUtils.showErrorCustomToast(this@ClassSubjectsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<AllClassesResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@ClassSubjectsActivity, t.message.toString())
            }
        })
    }


    private fun showClasses() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Class"

        val classNamesList = classList.map { it.class_name }

        var selectedClassName: String? = null
        var selectedClassId: String? = null

        // Set up adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, classNamesList)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Pre-select if a class was previously selected
        val preSelectedIndex = classList.indexOfFirst { it.class_name == selected_class_names }
        if (preSelectedIndex != -1) {
            listView.setItemChecked(preSelectedIndex, true)
            selectedClassName = classList[preSelectedIndex].class_name
            selectedClassId = classList[preSelectedIndex].id
        }

        // Handle single selection
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedClassName = classList[position].class_name
            selectedClassId = classList[position].id
        }

        // Show AlertDialog
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                if (selectedClassName != null && selectedClassId != null) {
                    selected_class_names = selectedClassName!!
                    selected_class_ids = selectedClassId!!.toString()
                    binding.classTxt.text = selected_class_names

                    callClassSubjectList()

                    Log.d("SelectedClass", selected_class_ids.toString())
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }


}