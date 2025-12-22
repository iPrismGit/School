package com.iprism.school.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.AttandanceStudentsAdapter
import com.iprism.school.databinding.ActivityAttendanceBinding
import com.iprism.school.model.Request.AttandanceUpdateReq
import com.iprism.school.model.Request.ClassStudentsReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.AttandanceStudentResponse
import com.iprism.school.model.Response.AttendanceUpdatedResponse
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AttendanceActivity : BaseActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private var attendanceType: String = "pending"
    private var selectedDate = ""
    private lateinit var crossImage: ImageView
    private lateinit var attendanceCrossImage: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var applyBtn: Button
    private lateinit var markBtn: Button
    private lateinit var attendanceCancelBtn: Button
    private lateinit var dateLo: ConstraintLayout
    private lateinit var dateTxt: TextView

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseList>()

    private var selected_class_ids : String? = ""
    private var selected_class_names : String? = ""

    private var notification_parent : String? = ""

    private var total_present_students : String? = ""
    private var total_absent_students : String? = ""

    private var selectedCount: Int = 0
    private var unselectedCount: Int = 0

    private lateinit var studentsAdapter: AttandanceStudentsAdapter

    private var selectedStudentIds: String = ""
    private var unselectedStudentIds: String = ""
    private var isAllSelected = false // Track select all state


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        handleBack()
//        handlePendingLo()
//        handleRejectedLo()
        handleEditBtn()
        handleSaveAttendanceBtn()

        callclasses()

        binding.dateLl.setOnClickListener {
            showDatePickerDialog()
        }

        binding.classLl.setOnClickListener {
            showClasses()
        }

        binding.parentNotificationCb.setOnCheckedChangeListener { _, isChecked ->
            notification_parent = if (isChecked) "yes" else "no"
            Log.d("NotifyValue", "Notify is: $notification_parent") // For debugging
        }

        // Select All checkbox listener
        binding.checkBoxAll.setOnCheckedChangeListener { _, isChecked ->
            isAllSelected = isChecked
            studentsAdapter.toggleSelectAll(isAllSelected)
        }

        binding.saveAttendanceBtn.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids ==  null){
                showToast("select class".toString())
            }else if (binding.dateTxt.text.toString() == ""||binding.dateTxt.text.toString()== null){
                showToast("select Date".toString())
            } else if (selectedStudentIds == ""||selectedStudentIds == null){
                showToast("select students".toString())
            }else{
                callStudentsAttandanceUpdate()
            }
        }

    }

    private fun handleSaveAttendanceBtn() {
        binding.saveAttendanceBtn.setOnClickListener(View.OnClickListener {
            showAttendanceConformationBottomSheet()
        })
    }

    private fun handleEditBtn() {
        binding.editIv.setOnClickListener(View.OnClickListener {
            showClassesBottomSheet()
        })
    }

//    private fun handleRejectedLo() {
//        binding.rejectedLo.setOnClickListener(View.OnClickListener {
//            binding.rejectedTxt.setTextColor(resources.getColor(R.color.blue3))
//            binding.rejectedCountTxt.setTextColor(resources.getColor(R.color.blue3))
//            binding.pendingTxt.setTextColor(resources.getColor(R.color.gray1))
//            binding.pendingCountTxt.setTextColor(resources.getColor(R.color.gray1))
//            attendanceType = "rejected"
//        })
//    }
//
//    private fun handlePendingLo() {
//        binding.pendingLo.setOnClickListener(View.OnClickListener {
//            binding.pendingTxt.setTextColor(resources.getColor(R.color.blue3))
//            binding.pendingCountTxt.setTextColor(resources.getColor(R.color.blue3))
//            binding.rejectedTxt.setTextColor(resources.getColor(R.color.gray1))
//            binding.rejectedCountTxt.setTextColor(resources.getColor(R.color.gray1))
//            attendanceType = "pending"
//        })
//    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AttendanceActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun showAttendanceConformationBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.all_students_present_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        attendanceCancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        attendanceCrossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        markBtn = bottomSheetDialog.findViewById<View>(R.id.mark_button) as Button
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        attendanceCancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        attendanceCrossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        markBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Attendance Marked Successfully")
        })

        bottomSheetDialog.show()
    }

    private fun showClassesBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.switch_user_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        applyBtn = bottomSheetDialog.findViewById<View>(R.id.apply_button) as Button
        dateLo = bottomSheetDialog.findViewById<View>(R.id.date_lo) as ConstraintLayout
        dateTxt = bottomSheetDialog.findViewById<View>(R.id.date_txt) as TextView
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(dateTxt, false)
        })

        cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        crossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        applyBtn.setOnClickListener(View.OnClickListener {
            selectedDate = dateTxt.text.toString()


            bottomSheetDialog.dismiss()
            binding.dateTxt.text = selectedDate
            Log.d("SelectedDate", selectedDate)
        })

        bottomSheetDialog.show()
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format and display the selected date in the EditText
                val formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear)
                binding.selecteddate.text = formattedDate
                callStudents()
            },
            year,
            month,
            day
        )

        // Restrict the calendar to prevent future dates
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val date = Calendar.getInstance()
        date.set(year, month - 1, day)  // month is zero-based in Calendar

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date.time)
    }

    private fun callclasses() {
        showProgress()
        var loginApiRequest = TeacherAccessReq( teacherId,auth_token)
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
                        ToastUtils.showSuccessCustomToast(this@AttendanceActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@AttendanceActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@AttendanceActivity, response.message())
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@AttendanceActivity, t.message.toString())
            }
        })
    }

    private fun showClasses() {
        val classNam = classList.map { it.class_name }.toTypedArray()

        val selectedSectionIndex = classList.indexOfFirst { classIds.contains(it.id) }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Select Class")
            .setSingleChoiceItems(classNam, selectedSectionIndex) { _, which ->
                // Update the selected section based on user choice
                classIds.clear()
                classNames.clear()
                classIds.add(classList[which].id)
                classNames.add(classList[which].class_name)
            }
            .setPositiveButton("OK") { _, _ ->
                // Update UI and log the selection
                selected_class_ids = classIds.joinToString("")
                selected_class_names = classNames.joinToString("")
                binding.selectedclass.text = selected_class_names.toString()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancel action if needed
                selected_class_ids = ""
                selected_class_names = ""
                Log.d("SelectedSection", "Selection cancelled")
            }
            .create()
        dialog.show()

    }

    private fun callStudents() {
        showProgress()
        var loginApiRequest = ClassStudentsReq(auth_token,selected_class_ids.toString(),binding.selecteddate.text.toString(),scl_id,teacherId)
        Log.d("class_students_Req", loginApiRequest.toString())
        val call: Call<AttandanceStudentResponse> = parentApiService!!.attandanceStudents(loginApiRequest)
        call.enqueue(object : Callback<AttandanceStudentResponse> {
            override fun onResponse(call: Call<AttandanceStudentResponse>, response: Response<AttandanceStudentResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    Log.d("studentsResponseList", loginApiResponse.toString())

                    if (loginApiResponse != null && loginApiResponse.status) {
                        if (loginApiResponse.response.attendance.isEmpty()) {
                            binding.nodataTv.visibility = View.VISIBLE
                            binding.studentAttendanceRv.visibility = View.GONE
                        } else {
                            binding.nodataTv.visibility = View.GONE
                            binding.studentAttendanceRv.visibility = View.VISIBLE

//                            total_present_students = loginApiResponse.response.attendance[0].total_present_students.toString()
//                            total_absent_students = loginApiResponse.response.attendance[0].total_absent_students.toString()
//
                            studentsAdapter = AttandanceStudentsAdapter(this@AttendanceActivity,
                                loginApiResponse.response.attendance,
                                { selectedIdsList, unselectedIdsList,selectedSize, unselectedSize ->
                                    selectedStudentIds = selectedIdsList.joinToString(",")
                                    unselectedStudentIds = unselectedIdsList.joinToString(",")

                                    selectedCount = selectedSize
                                    unselectedCount = unselectedSize

                                    binding.presentCountTxt.text = selectedCount.toString()
                                    binding.absentCountTxt.text = unselectedCount.toString()

                                    Log.d("Selected_IDs", selectedStudentIds)
                                    Log.d("Unselected_IDs", unselectedStudentIds)
                                },
                                selectAll = false
                            )
                            binding.studentAttendanceRv.adapter = studentsAdapter
                            var layoutManager = LinearLayoutManager(this@AttendanceActivity)
                            binding.studentAttendanceRv.layoutManager = layoutManager
                        }
                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.studentAttendanceRv.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@AttendanceActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@AttendanceActivity, response.message())
                }
            }

            override fun onFailure(call: Call<AttandanceStudentResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@AttendanceActivity, t.message.toString())
            }
        })
    }


    private fun callStudentsAttandanceUpdate() {
        showProgress()
        var loginApiRequest = AttandanceUpdateReq(unselectedStudentIds,auth_token,selected_class_ids.toString()
            ,binding.selecteddate.text.toString(),selectedStudentIds,scl_id,notification_parent.toString(),
            teacherId,unselectedCount.toString(),selectedCount.toString())
        Log.d("update_attendance_Req", loginApiRequest.toString())
        val call: Call<AttendanceUpdatedResponse> = parentApiService!!.updateAttandanceStudents(loginApiRequest)
        call.enqueue(object : Callback<AttendanceUpdatedResponse> {
            override fun onResponse(call: Call<AttendanceUpdatedResponse>, response: Response<AttendanceUpdatedResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse != null && loginApiResponse.status) {

//                        showToast(loginApiResponse.message.toString())

                         val intent = Intent(this@AttendanceActivity, AttendanceActivity::class.java)
                        startActivity(intent)
                        finish()

//                        callStudents()

                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@AttendanceActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@AttendanceActivity, response.message())
                }
            }

            override fun onFailure(call: Call<AttendanceUpdatedResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@AttendanceActivity, t.message.toString())
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AttendanceActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}