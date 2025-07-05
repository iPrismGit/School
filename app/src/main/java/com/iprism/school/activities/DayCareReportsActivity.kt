package com.iprism.school.activities

import android.app.DatePickerDialog
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.DaycareReportActivity
import com.iprism.school.activities.calender.CalenderActivity
import com.iprism.school.activities.calender.CalenderDetailsActivity
import com.iprism.school.adapters.DayCareReportsStudentsListAdapter
import com.iprism.school.adapters.DayCareStudentsAdapter
import com.iprism.school.databinding.ActivityDayCareReportsBinding
import com.iprism.school.databinding.ActivityDaycareReportBinding
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherGroupStudentsReq
import com.iprism.school.model.Response.DayCareReportsStudentsResponse
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.GroupsTeacher
import com.iprism.school.model.Response.TeacherGroupStudentsResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DayCareReportsActivity : BaseActivity() {

    private lateinit var binding: ActivityDayCareReportsBinding

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var id: String = ""
    private var name: String = ""
    private var type: String = ""
    private var group_id: String = ""

    private val groupNames = mutableListOf<String>()
    private val groupIds = mutableListOf<String>()
    private val groupList = mutableListOf<GroupsTeacher>()

    private var selected_group_ids : String? = ""
    private var selected_group_names : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayCareReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        callGroups()

        binding.dateLo.setOnClickListener {
            showDatePickerDialog()
        }

        binding.groupLl.setOnClickListener {
            showGroups()
        }

    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format and display the selected date in the EditText
                val formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear)
                binding.dateTxt.text = formattedDate.toString()
                callStudentsnew()
            },
            year,
            month,
            day
        )

        // Restrict the calendar to prevent future dates
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
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
                        ToastUtils.showSuccessCustomToast(this@DayCareReportsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@DayCareReportsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DayCareReportsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DayCareReportsActivity, t.message.toString())
            }
        })
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
                binding.selectedgroup.text = selected_group_names
                Log.d("selectedgroups", selected_group_ids.toString())

                callStudentsnew()

            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                groupIds.clear()
                groupList.clear()
                selected_group_ids = ""
                selected_group_names = ""
                binding.selectedgroup.text =""
            }
            .create()
        dialog.show()
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val date = Calendar.getInstance()
        date.set(year, month - 1, day)  // month is zero-based in Calendar

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date.time)
    }

    private fun callStudentsnew() {
        showProgress()
        var loginApiRequest = TeacherGroupStudentsReq( auth_token,binding.dateTxt.text.toString(),selected_group_ids.toString(),scl_id,teacherId,"report")
        Log.d("report_students_Req", loginApiRequest.toString())
        val call: Call<DayCareReportsStudentsResponse> = parentApiService!!.reportsStudents(loginApiRequest)
        call.enqueue(object : Callback<DayCareReportsStudentsResponse> {
            override fun onResponse(
                call: Call<DayCareReportsStudentsResponse>,
                response: Response<DayCareReportsStudentsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse != null && loginApiResponse.status) {
                        if (loginApiResponse.response.groups.isEmpty()) {
                            binding.nodataTv.visibility = View.VISIBLE
                            binding.dayCareReportsRv.visibility = View.GONE
                        } else {
                            binding.nodataTv.visibility = View.GONE
                            binding.dayCareReportsRv.visibility = View.VISIBLE

                            var dairiesAdapter = DayCareReportsStudentsListAdapter(this@DayCareReportsActivity,loginApiResponse.response.groups)
                            binding.dayCareReportsRv.adapter = dairiesAdapter
                            var layoutManager = LinearLayoutManager(this@DayCareReportsActivity)
                            binding.dayCareReportsRv.layoutManager = layoutManager


                            dairiesAdapter.OnItemBtn = {
                                    mydata ->
                                val studentId = mydata.id.toString()
                                val intent = Intent(this@DayCareReportsActivity, DaycareReportDetailsActivity::class.java)
                                intent.putExtra("studentId",studentId)
                                intent.putExtra("selected_date",binding.dateTxt.text.toString())
                                startActivity(intent)
                            }
                        }
                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.dayCareReportsRv.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@DayCareReportsActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DayCareReportsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<DayCareReportsStudentsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DayCareReportsActivity, t.message.toString())
            }
        })
    }


}