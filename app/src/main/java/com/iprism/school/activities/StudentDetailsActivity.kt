package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.FillOtherDetailsActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.adapters.StudentsListAdapter
import com.iprism.school.databinding.ActivityStudentDetailsBinding
import com.iprism.school.databinding.DeactiveStudentBottomSheetBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.model.Request.DeleteStduentReq
import com.iprism.school.model.Request.StudentDetailsReq
import com.iprism.school.model.Request.StudentsListReq
import com.iprism.school.model.Response.StudentDeleteResponse
import com.iprism.school.model.Response.StudentDetailsResponse
import com.iprism.school.model.Response.StudentListResponse
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentDetailsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var message_id: String = ""
    private var student_Type: String = "active"
    private var type: String = ""
    private var studentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        message_id = intent.getStringExtra("message_id").toString()
        studentId = intent.getStringExtra("studentId").toString()

        handleBack()
        handleDeActiveStudentIv()
        callStudentDetails()

        binding.deleteButton.setOnClickListener {
            callDelete()
        }

    }

    private fun handleDeActiveStudentIv() {
        binding.deactiveIv.setOnClickListener(View.OnClickListener {
            showDeactivateBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showDeactivateBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DeactiveStudentBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        binding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.deactivateBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Student Deactivated Successfully!")
        })
        bottomSheetDialog.show()
    }

    private fun callStudentDetails() {
        showProgress()
        var apiRequest = StudentDetailsReq(auth_token,scl_id,studentId,teacherId)
        Log.d("studentDetails", apiRequest.toString())
        val call: Call<StudentDetailsResponse> = parentApiService!!.studentsDetails(apiRequest)
        call.enqueue(object : Callback<StudentDetailsResponse> {
            override fun onResponse(call: Call<StudentDetailsResponse>, response: Response<StudentDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        studentId = loginApiResponse.response.student_details.id.toString()

                        Glide.with(this@StudentDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.student_details.student_image)
                            .into(binding.proImg)

                        binding.nameTv.text = loginApiResponse.response.student_details.student_name.toString()
                        binding.admissionIdTv.text = loginApiResponse.response.student_details.admission_id.toString()
                        binding.classTv.text = loginApiResponse.response.student_details.class_name.toString()
                        binding.dobTv.text = loginApiResponse.response.student_details.student_dob.toString()

                        binding.genderTv.text = loginApiResponse.response.student_details.student_gender.toString()
                        binding.fatherNameTv.text = loginApiResponse.response.student_details.father_name.toString()
                        binding.motherNameTv.text = loginApiResponse.response.student_details.mother_name.toString()
                        binding.motherMobileTv.text = loginApiResponse.response.student_details.mother_mobile.toString()
                        binding.fatheroccupationTv.text = loginApiResponse.response.student_details.father_occupation.toString()
                        binding.emergencyNameTv.text = loginApiResponse.response.student_details.emergency_contact_person.toString()
                        binding.emergencymobileTv.text = loginApiResponse.response.student_details.emergency_contact_contact.toString()
                        binding.dojTv.text = loginApiResponse.response.student_details.joining_date.toString()
                        binding.addressTv.text = loginApiResponse.response.student_details.address.toString()
                        binding.castTv.text = loginApiResponse.response.student_details.caste.toString()
                        binding.securityAmountTv.text = loginApiResponse.response.student_details.security_amount.toString()
                        binding.groupsTv.text = loginApiResponse.response.student_details.group_names.toString()
                        binding.bloodGroupTv.text = loginApiResponse.response.student_details.student_blood_group.toString()

                        binding.editStudentIv.setOnClickListener {
                            val intent = Intent(this@StudentDetailsActivity, FillSchoolDetailsActivity::class.java)
                            intent.putExtra("studentId",studentId)
                            intent.putExtra("admission_id",loginApiResponse.response.student_details.admission_id.toString())
                            intent.putExtra("sessionName",loginApiResponse.response.student_details.session_name.toString())
                            intent.putExtra("session_id",loginApiResponse.response.student_details.session_id.toString())
                            intent.putExtra("class_id",loginApiResponse.response.student_details.class_id.toString())
                            intent.putExtra("class_name",loginApiResponse.response.student_details.class_name.toString())
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StudentDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StudentDetailsActivity, t.message.toString())
            }
        })
    }


    private fun callDelete() {
        showProgress()
        var apiRequest = DeleteStduentReq(auth_token,scl_id,studentId,teacherId)
        Log.d("deleteStudentReq", apiRequest.toString())
        val call: Call<StudentDeleteResponse> = parentApiService!!.deleteStudent(apiRequest)
        call.enqueue(object : Callback<StudentDeleteResponse> {
            override fun onResponse(call: Call<StudentDeleteResponse>, response: Response<StudentDeleteResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        val intent = Intent(this@StudentDetailsActivity, StudentsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StudentDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDeleteResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StudentDetailsActivity, t.message.toString())
            }
        })
    }

}