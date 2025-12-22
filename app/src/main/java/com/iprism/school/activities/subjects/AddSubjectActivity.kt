package com.iprism.school.activities.subjects

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityAddSubjectBinding
import com.iprism.school.model.Request.CreateSubjectReq
import com.iprism.school.model.Request.UpdateSubjectReq
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSubjectActivity : BaseActivity() {

    private lateinit var binding: ActivityAddSubjectBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var selected_Type : String? = ""
    private var subId : String? = ""
    private var subName : String? = ""
    private var subType : String? = ""
    private var subDescription : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()


        binding.crossIv.setOnClickListener {
            val  intent = Intent(this@AddSubjectActivity, SubjectsActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (tag == "edit"){
            binding.titleTv.text = "Update Subject"
            subId = intent.getStringExtra("subId").toString()
            subName = intent.getStringExtra("subName").toString()
            subType = intent.getStringExtra("subType").toString()
            selected_Type = intent.getStringExtra("subType").toString()

            subDescription = intent.getStringExtra("subDescription").toString()
            binding.subNameEt.setText(subName)
            binding.selectedgenderTv.setText(subType)
            binding.subDescriptionEt.setText(subDescription)
        }else{
            binding.titleTv.text = "Add Subject"
        }

        val genderoptions = arrayOf("Grade", "Marks")
        binding.genderll.setOnClickListener {
            // Track the selected option
            var selectedOption = ""
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an Option")
            builder.setSingleChoiceItems(genderoptions, -1) { dialog, which ->
                selectedOption = genderoptions[which] // Capture the selected option
            }
            builder.setPositiveButton("OK") { dialog, _ ->
                if (selectedOption.isNotEmpty()) {
                    selected_Type = selectedOption.toString()
                    binding.selectedgenderTv.text = selectedOption.toString()
//                    Toast.makeText(this, "You selected: $selectedOption", Toast.LENGTH_SHORT).show()
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


        binding.submitBtn.setOnClickListener {
            if (binding.subNameEt.text.toString() == ""||binding.subNameEt.text.toString() == null){
                showToast("Enter Subject Name")
            }else if (selected_Type == ""||selected_Type == null){
                showToast("Select Type")
            }else if (binding.subDescriptionEt.text.toString() == ""||binding.subDescriptionEt.text.toString() == null){
                showToast("Enter Description")
            }else{

                if (tag == "edit"){
                    updateSubject()
                }else{
                    addSubject()
                }
            }
        }
    }

    private fun addSubject() {
        showProgress()
        var apiRequest = CreateSubjectReq(auth_token,binding.subDescriptionEt.text.toString(),scl_id,binding.subNameEt.text.toString(),selected_Type.toString(),teacherId)
        Log.d("addSubject", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.createSubject(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@AddSubjectActivity, SubjectsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@AddSubjectActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@AddSubjectActivity, t.message.toString())
            }
        })
    }


    private fun updateSubject() {
        showProgress()
        var apiRequest = UpdateSubjectReq(auth_token,binding.subDescriptionEt.text.toString(),scl_id,subId.toString(),binding.subNameEt.text.toString(),selected_Type.toString(),teacherId)
        Log.d("updateSubject", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.updateSubject(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent= Intent(this@AddSubjectActivity, SubjectsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@AddSubjectActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@AddSubjectActivity, t.message.toString())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val  intent = Intent(this@AddSubjectActivity, SubjectsActivity::class.java)
        startActivity(intent)
        finish()
    }

}