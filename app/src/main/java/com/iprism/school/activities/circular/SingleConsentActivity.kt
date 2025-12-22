package com.iprism.school.activities.circular

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.ConsentAttachmentsAdapter
import com.iprism.school.databinding.ActivitySingleConsentBinding
import com.iprism.school.model.Request.SingleConsentViewReq
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel

class SingleConsentActivity : BaseActivity() {

    private lateinit var binding: ActivitySingleConsentBinding
    private var isInfoVisible: Boolean = false
    private lateinit var crossImage: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var deleteBtn: Button

    private val viewModel: Scl_ViewModel by viewModels()
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var consentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        consentId = intent.getStringExtra("consentId").toString()

        handleBack()
        handleDownArrow()
        handleDeleteBtn()
        handleEditBtn()
        handleInfoBtn()
        handleEmailBtn()

        consentDetails()

    }

    private fun handleEmailBtn() {
        binding.emailIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ConsentEmailReportActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleInfoBtn() {
        binding.infoIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ConsentInfoActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleEditBtn() {
        binding.editIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, EditConsentActivity::class.java)
            intent.putExtra("consentId",consentId.toString())
            startActivity(intent)
        })
    }

    private fun handleDeleteBtn() {
        binding.trashIv.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun handleDownArrow() {
        binding.dropDownIv.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.infoLo.visibility = View.GONE
            binding.dropDownIv.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.infoLo.visibility = View.VISIBLE
            binding.dropDownIv.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }


    private fun consentDetails() {
        showProgress()
        var apiRequest = SingleConsentViewReq(auth_token,consentId,"",scl_id,teacherId)
        Log.d("consentSingleViewReq", apiRequest.toString())
        viewModel.singleConsentView(apiRequest).observe(this@SingleConsentActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                Log.d("consentSingleViewRes", response.toString())

                binding.namesTv.text = response.response.consent_details[0].class_names.toString()
                binding.dateandTime.text = response.response.consent_details[0].created_date.toString()+" , "+response.response.consent_details[0].calender_time.toString()
                binding.studentsTv.text = "Student Names : "+response.response.consent_details[0].student_names.toString()
                binding.groupsTv.text = "Group Names : "+response.response.consent_details[0].group_names.toString()

                binding.rvAttachment.layoutManager = GridLayoutManager(this,3)
                val adapter = ConsentAttachmentsAdapter(this@SingleConsentActivity,response.response.attachments)
                binding.rvAttachment.adapter = adapter
                adapter.notifyDataSetChanged()

                adapter.OnItemCallPic = {
                        mydata ->
//                    val images = Constants.IMAGES_URL+mydata.attachment.toString()
//                    Log.d("images2025",images.toString())
//                    showFullView(images)
                }

                adapter.OnItemCalldelete = {
                        mydata ->
//                    val picId = mydata.id.toString()
//                    callDeleteImg(picId)
                }

            } else {
                hideProgress()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@SingleConsentActivity, LoginActivity::class.java))
                    finish()
                }else{

                }
            }
        })
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.delete_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        deleteBtn = bottomSheetDialog.findViewById<View>(R.id.delete_button) as Button
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        crossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBtn.setOnClickListener(View.OnClickListener {
            callDelete(bottomSheetDialog)
//            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        })

        bottomSheetDialog.show()
    }
    private fun callDelete(bottomSheetDialog: BottomSheetDialog) {
        showProgress()
        var apiRequest = SingleConsentViewReq(auth_token, consentId, "", scl_id,teacherId)
        Log.d("calenderDeleteReq", apiRequest.toString())
        viewModel.consentDelete(apiRequest)
            .observe(this@SingleConsentActivity, Observer { response ->
                if (response != null && response.status == true) {
                    hideProgress()
                    bottomSheetDialog.dismiss()
                    val intent = Intent(this@SingleConsentActivity, ConsentsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    hideProgress()
                    bottomSheetDialog.dismiss()
                    if (response!!.message.toString() == "Authentication Token Expired") {
                        user!!.storeUserDetails(
                            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
                        )
                        startActivity(
                            Intent(this@SingleConsentActivity, LoginActivity::class.java))
                        finish()
                    } else {

                    }
                }
            })
    }

}