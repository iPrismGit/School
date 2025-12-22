package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillSchoolDetailsBinding
import com.iprism.school.model.Request.AllclassesReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.AdmissionResponse
import com.iprism.school.model.Response.AllClassesResponse
import com.iprism.school.model.Response.ClasseAllList
import com.iprism.school.model.Response.SessionList
import com.iprism.school.model.Response.SessionListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FillSchoolDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityFillSchoolDetailsBinding
    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseAllList>()

    private var selected_class_ids : String? = ""
    private var selected_class_names : String? = ""

    private val sessionNames = mutableListOf<String>()
    private val sessionIds = mutableListOf<String>()
    private val sessionList = mutableListOf<SessionList>()

    private var selected_session_ids : String? = ""
    private var selected_session_names : String? = ""

    private var admissionId : String? = ""
    private var studentId : String? = ""
    private var sessionName : String? = ""
    private var session_id : String? = ""
    private var class_id : String? = ""
    private var class_name : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillSchoolDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tag = intent.getStringExtra("tag").toString()

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleBack()

        if (tag == "edit"){

            binding.nextButton.text = "Update"

            studentId = intent.getStringExtra("studentId").toString()

            admissionId = intent.getStringExtra("admission_id").toString()
            selected_session_names = intent.getStringExtra("sessionName").toString()
            selected_session_ids = intent.getStringExtra("session_id").toString()
            selected_class_ids = intent.getStringExtra("class_id").toString()
            selected_class_names = intent.getStringExtra("class_name").toString()

            binding.admissionId.text = admissionId.toString()
            binding.selectedClassTv.text = selected_class_names.toString()
            binding.sessionTv.text = selected_session_names.toString()

        }else{
            binding.nextButton.text = "Next"
        }

        callclasses()

        binding.generateIdBtn.setOnClickListener {
            generateId()
        }

        binding.sessionLl.setOnClickListener {
            showsession()
        }

        binding.classLl.setOnClickListener {
            showClasses()
        }

        binding.nextButton.setOnClickListener(View.OnClickListener {
            if (admissionId  == ""||admissionId == null){
                showToast("Generate Id")
            }else if (selected_session_ids == ""||selected_session_ids == null){
                showToast("Select Session")
            }else if (selected_class_ids == null ||selected_class_ids == null){
                showToast("Select Class")
            }else {
                var intent = Intent(this, FillPersonalDetailsActivity::class.java)
                intent.putExtra("admissionId",admissionId)
                intent.putExtra("studentId",studentId)
                intent.putExtra("tag",tag)
                intent.putExtra("selected_session_ids",selected_session_ids)
                intent.putExtra("selected_class_ids",selected_class_ids)
                startActivity(intent)
            }
        })
    }

    private fun generateId() {
        showProgress()
        var apiRequest =SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("studentId", apiRequest.toString())
        val call: Call<AdmissionResponse> = parentApiService!!.generateStudentId(apiRequest)
        call.enqueue(object : Callback<AdmissionResponse> {
            override fun onResponse(call: Call<AdmissionResponse>, response: Response<AdmissionResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        admissionId = loginApiResponse.response.admission_id.toString()
                        binding.admissionId.text = admissionId.toString()

                    }else{
                        showToast("failure1")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillSchoolDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<AdmissionResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillSchoolDetailsActivity, t.message.toString())
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FillSchoolDetailsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
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
                        callsession()
                    }
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@FillSchoolDetailsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@FillSchoolDetailsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillSchoolDetailsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<AllClassesResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillSchoolDetailsActivity, t.message.toString())
            }
        })
    }

    private fun callsession() {
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
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@FillSchoolDetailsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@FillSchoolDetailsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillSchoolDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SessionListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillSchoolDetailsActivity, t.message.toString())
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
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                classNames.clear()
                classIds.clear()
                classNames.addAll(tempClassNames)
                classIds.addAll(tempClassIds)

                selected_class_ids = classIds.joinToString(",")
                selected_class_names = classNames.joinToString(",")
                binding.selectedClassTv.text = selected_class_names
                Log.d("SelectedClass", selected_class_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                classNames.clear()
                classIds.clear()
                selected_class_ids = ""
                selected_class_names = ""
                binding.selectedClassTv.text = ""
            }
            .create()
        dialog.show()
    }

    private fun showsession() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Students"

        val originalClassNames = mutableListOf("Select All") + sessionList.map { it.session_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = sessionNames.toMutableSet()
        val tempClassIds = sessionIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        sessionList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == sessionList.size) {
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
                    tempClassNames.addAll(sessionList.map { it.session_name })
                    tempClassIds.addAll(sessionList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = sessionList[which - 1].session_name
                val selectedClassId = sessionList[which - 1].id

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
                checkedItems[0] = tempClassNames.size == sessionList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(sessionList.map { it.session_name })
                } else {
                    filteredClassNames.addAll(sessionList.filter { it.session_name.contains(newText, true) }.map { it.session_name })
                }
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        // Build and Show AlertDialog
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                sessionNames.clear()
                sessionIds.clear()
                sessionNames.addAll(tempClassNames)
                sessionIds.addAll(tempClassIds)

                selected_session_ids = sessionIds.joinToString(",")
                selected_session_names = sessionNames.joinToString(" , ")
                binding.sessionTv.text = selected_session_names
                Log.d("selectedStudents", selected_session_ids.toString())

            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                sessionNames.clear()
                sessionIds.clear()
                selected_session_names = ""
                selected_session_ids = ""
//                binding.selectedstudent.text = ""
            }
            .create()
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@FillSchoolDetailsActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}