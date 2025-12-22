package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillHealthDetailsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.ChildHasHadBottomSheetBinding
import com.iprism.school.databinding.ChildSuffersFromBottomSheetBinding
import com.iprism.school.model.Request.StudentDetailsReq
import com.iprism.school.model.Request.StudentOtherDetailsReq
import com.iprism.school.model.Response.StudentDetailsResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FillHealthDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityFillHealthDetailsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var student_id: String = ""

    private val childhadItems = mutableListOf<String>()
    private val childhagItemIds = mutableListOf<Int>()
//    private val childhadList = mutableListOf<Diseases1>()

    private val childsufferItems = mutableListOf<String>()
    private val childsufferItemIds = mutableListOf<Int>()
//    private val childsufferList = mutableListOf<Diseases2>()

    private var selectedChild_had_ids : String? = ""
    private var selectedChild_had_names : String? = ""

    private var selectedChild_suffer_ids : String? = ""
    private var selectedChild_suffer_names : String? = ""

    private var sAmount : String? = ""
    private var selectedCab_ids : String? = ""
    private var selectedGroup_ids : String? = ""

    private var selectd_childHadHas : String? = ""
    private var selected_childsufferFrom : String? = ""

    private var dentistName : String? = ""
    private var dentistMobile : String? = ""
    private var dentistEmailId : String? = ""
    private var otherAllergies : String? = ""
    private var specialCondition : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillHealthDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        student_id = intent.getStringExtra("student_id").toString()


        if (tag == "edit"){
            callStudentDetails()
        }

        Log.d("tagggg",tag)
        sAmount = intent.getStringExtra("sAmount").toString()
        selectedCab_ids = intent.getStringExtra("selectedCab_ids").toString()
        selectedGroup_ids = intent.getStringExtra("selectedGroup_ids").toString()

        handleBackBtn()
        handleAddStudentBtn()
        handleAddMoreBtn()

        val genderOptions = arrayOf("Measles", "German", "Chicken Pox","Mumps","Whopping Cough")
        val selectedItems = mutableListOf<String>() // List to store selected items

        binding.childHasHadLl.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Child Has Had")

            val selectedBooleans = BooleanArray(genderOptions.size) // Track selected options

            builder.setMultiChoiceItems(genderOptions, selectedBooleans) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(genderOptions[which]) // Add selected item
                } else {
                    selectedItems.remove(genderOptions[which]) // Remove deselected item
                }
            }

            builder.setPositiveButton("OK") { dialog, _ ->
                if (selectedItems.isNotEmpty()) {
                    val selectedString = selectedItems.joinToString(", ") // Convert list to comma-separated string
                    selectedChild_had_ids = selectedString
                    binding.childHasHadTv.text = selectedString
                } else {
                    Toast.makeText(this, "No option selected", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }


        val genderOption = arrayOf("Headaches", "Earaches", "Sore Throat","Stomach Pain","Flu/Cold")
        val selectedItem = mutableListOf<String>() // List to store selected items

        binding.childSuffersLl.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Child Suffers From")

            val selectedBooleans = BooleanArray(genderOption.size) // Track selected options

            builder.setMultiChoiceItems(genderOption, selectedBooleans) { _, which, isChecked ->
                if (isChecked) {
                    selectedItem.add(genderOption[which]) // Add selected item
                } else {
                    selectedItem.remove(genderOption[which]) // Remove deselected item
                }
            }

            builder.setPositiveButton("OK") { dialog, _ ->
                if (selectedItem.isNotEmpty()) {
                    val selectedString = selectedItem.joinToString(",") // Convert list to comma-separated string
                    selectedChild_suffer_ids= selectedString
                    binding.childSufferFromTv.text = selectedString
                } else {
                    Toast.makeText(this, "No option selected", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }
    }

    private fun handleAddMoreBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            showAddMoreBottomSheet()
        })
    }

    private fun handleAddStudentBtn() {
            binding.addStudentButton.setOnClickListener {
                hideKeyboard()
                if (tag == "edit"){
//                    createStudent()
                }else{
                    if (binding.etRegularMedication.text.toString() == ""||binding.etRegularMedication.text.toString() == null){
                        showToast("Enter Regular Medication")
                    }else if (binding.etAllergies.text.toString() == ""||binding.etAllergies.text.toString() == null){
                        showToast("Enter Medication Allergies")
                    } else if (binding.etFoodAllergies.text.toString() == ""||binding.etFoodAllergies.text.toString() == null){
                        showToast("Enter Food Allergies")
                    }else if (binding.etPhysician.text.toString()== ""||binding.etPhysician.text.toString()==null){
                        showToast("Enter Physician Name")
                    }else if (binding.etPhysicianMobile.text.toString() == ""||binding.etPhysicianMobile.text.toString() == null){
                        showToast("Enter Physician Mobile")
                    } else if (binding.etPreHospital.text.toString() == ""||binding.etPreHospital.text.toString() == null){
                        showToast("Enter Preferred Hospital")
                    } else if (binding.etHospitalContact.text.toString() == ""||binding.etHospitalContact.text.toString() == null){
                        showToast("Enter Preferred Hospital Mobile")
                    } else if (selectedChild_had_ids == ""||selectedChild_had_ids == null){
                        showToast("Select Child Has Had")
                    } else if (selectedChild_suffer_ids == ""||selectedChild_suffer_ids == null){
                        showToast("Select Child Suffer from")
                    } else {
                        createStudent()
                    }
                }
            }
    }

    private fun createStudent() {
        showProgress()
        var apiRequest = StudentOtherDetailsReq(auth_token,selectedCab_ids.toString(),
            dentistName.toString(),selectd_childHadHas.toString(),dentistEmailId.toString(),binding.etPhysician.text.toString(),
            childsufferItemIds.toString(),dentistMobile.toString()
            ,binding.etFoodAllergies.text.toString(),selectedGroup_ids.toString(),
            binding.etHospitalContact.text.toString(),binding.etAllergies.text.toString(),otherAllergies.toString(),
            binding.etPhysicianMobile.text.toString(),binding.etPreHospital.text.toString(),
            binding.etRegularMedication.text.toString(),scl_id,sAmount.toString(),specialCondition.toString(),student_id,teacherId)
        val call: Call<SuccessResponsePojo> = parentApiService!!.studentOtherDetails(apiRequest)
        Log.d("apiAddRequest",apiRequest.toString())
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@FillHealthDetailsActivity, FillSchoolDetailsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillHealthDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillHealthDetailsActivity, t.message.toString())
            }
        })
    }

    private fun handleBackBtn() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showChildHasHadBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = ChildHasHadBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showChildSuffersFromBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = ChildSuffersFromBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showAddMoreBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = AddMoreBottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()

                dentistName = binding.childDentistNameEt.text.toString()
                dentistMobile = binding.childDentistMobileEt.text.toString()
                dentistEmailId = binding.childDentistEmailEt.text.toString()
                otherAllergies = binding.childDentistOtherallergiesEt.text.toString()
                specialCondition = binding.childDentistSpecialConditionEt.text.toString()

                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                dentistName =""
                dentistMobile = ""
                dentistEmailId = ""
                otherAllergies = ""
                specialCondition = ""
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                dentistName =""
                dentistMobile = ""
                dentistEmailId = ""
                otherAllergies = ""
                specialCondition = ""
            })
        }
        bottomSheetDialog.show()
    }



    private fun callStudentDetails() {
        showProgress()
        var apiRequest = StudentDetailsReq(auth_token,scl_id,student_id.toString(),teacherId)
        Log.d("studentDetails4", apiRequest.toString())
        val call: Call<StudentDetailsResponse> = parentApiService!!.studentsDetails(apiRequest)
        call.enqueue(object : Callback<StudentDetailsResponse> {
            override fun onResponse(call: Call<StudentDetailsResponse>, response: Response<StudentDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        student_id = loginApiResponse.response.student_details.id.toString()

                        selectedCab_ids = loginApiResponse.response.student_details.cabs.toString()
                        selectedGroup_ids = loginApiResponse.response.student_details.groups.toString()

                        binding.etRegularMedication.setText(loginApiResponse.response.student_details.regular_medication.toString())
                        binding.etAllergies.setText(loginApiResponse.response.student_details.medicine_allergies.toString())
                        binding.etFoodAllergies.setText(loginApiResponse.response.student_details.food_allergies.toString())
                        binding.etPhysician.setText(loginApiResponse.response.student_details.child_physician.toString())
                        binding.etPhysicianMobile.setText(loginApiResponse.response.student_details.physician_conatct_no.toString())
                        binding.etPreHospital.setText(loginApiResponse.response.student_details.prefered_hosptal.toString())
                        binding.etHospitalContact.setText(loginApiResponse.response.student_details.hospiatl_contact_no.toString())

                        binding.childHasHadTv.setText(loginApiResponse.response.student_details.child_had_has.toString())
                        selectedChild_had_ids = loginApiResponse.response.student_details.child_had_has.toString()

                        binding.childSufferFromTv.setText(loginApiResponse.response.student_details.child_suffer_from.toString())
                        selectedChild_suffer_ids = loginApiResponse.response.student_details.child_suffer_from

                    }else{
                        hideProgress()
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillHealthDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillHealthDetailsActivity, t.message.toString())
            }
        })
    }

}