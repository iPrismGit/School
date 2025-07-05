package com.iprism.school.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding
import com.iprism.school.model.Request.AddAuthorizedReq
import com.iprism.school.model.Request.DeleteStduentReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.StudentDetailsReq
import com.iprism.school.model.Response.CabList
import com.iprism.school.model.Response.CabsListResponse
import com.iprism.school.model.Response.GroupList
import com.iprism.school.model.Response.GroupsListResponse
import com.iprism.school.model.Response.StudentDeleteResponse
import com.iprism.school.model.Response.StudentDetailsResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64

class FillOtherDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityFillOtherDetailsBinding
    private lateinit var crossImage: ImageView
    private lateinit var personPic: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var addPic: ConstraintLayout

    private lateinit var relationName: EditText
    private lateinit var relation: EditText

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var student_id: String = ""

    private val cabNames = mutableListOf<String>()
    private val cabIds = mutableListOf<String>()
    private val cabList = mutableListOf<CabList>()

    private var selectedCab_ids : String? = ""
    private var selectedCab_names : String? = ""

    private val  groupNames = mutableListOf<String>()
    private val groupIds = mutableListOf<String>()
    private val groupList = mutableListOf<GroupList>()

    private var selectedGroup_ids : String? = ""
    private var selectedGroup_names : String? = ""

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillOtherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        student_id = intent.getStringExtra("student_id").toString()

        if (tag == "edit"){
            callStudentDetails()
        }else{
            allCabs()
        }

        Log.d("tagggg",tag)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val bitmap = data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val b = baos.toByteArray()
                val encoder: Base64.Encoder = Base64.getEncoder()
                personPic.setImageBitmap(bitmap)
                encodedPic = encoder.encodeToString(b)
            }
        }

        resultLaunchergallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val uri = data?.data
                val imagestrem : InputStream? = contentResolver?.openInputStream(uri!!)
                val selectedImage  : Bitmap = BitmapFactory.decodeStream(imagestrem)
                personPic.setImageBitmap(selectedImage)
                encodedPic = encodeImage(selectedImage)
            }
        }


        handleBack()
        handleAddBtn()
        handleNextBtn()

        binding.selectCabsLl.setOnClickListener {
            selectCabs()
        }

        binding.selectgroupLL.setOnClickListener {
            selectGroups()
        }

    }

    private fun handleNextBtn() {
        binding.nextButton.setOnClickListener(View.OnClickListener {
            if (binding.amountEt.text.toString() == ""||binding.amountEt.text.toString() == null){
                showToast("Enter Security Amount")
            }else if (selectedCab_ids == ""||selectedCab_ids == null){
                showToast("Select Cab")
            }else if (selectedGroup_ids == ""||selectedGroup_ids == null){
                showToast("Select Group")
            }else{
                var intent = Intent(this, FillHealthDetailsActivity::class.java)
                intent.putExtra("sAmount",binding.amountEt.text.toString())
                intent.putExtra("selectedCab_ids",selectedCab_ids.toString())
                intent.putExtra("selectedGroup_ids",selectedGroup_ids.toString())
                intent.putExtra("student_id",student_id.toString())
                intent.putExtra("tag",tag.toString())
                startActivity(intent)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            showAuthorizedPersonBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            showbackDialog()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAuthorizedPersonBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.authorized_person_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        personPic = bottomSheetDialog.findViewById<View>(R.id.personPic) as ImageView
        okBtn = bottomSheetDialog.findViewById<View>(R.id.ok_button) as Button
        addPic = bottomSheetDialog.findViewById<View>(R.id.addPic_ll) as ConstraintLayout

         val nameEt = bottomSheetDialog.findViewById<View>(R.id.nameEt) as EditText
       val relationEt = bottomSheetDialog.findViewById<View>(R.id.relationEt) as EditText

        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        addPic.setOnClickListener {
            selectImage()
        }

        cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        crossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        okBtn.setOnClickListener(View.OnClickListener {
            addPersons(nameEt,relationEt,bottomSheetDialog)
            bottomSheetDialog.dismiss()
        })

        bottomSheetDialog.show()
    }

    private fun showbackDialog() {
        val dialogView = layoutInflater.inflate(R.layout.back_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        val btn_yes= dialogView.findViewById(R.id.yes_btn) as Button
        val btn_no = dialogView.findViewById(R.id.no_btn) as Button
        btn_no.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        btn_yes.setOnClickListener(View.OnClickListener {
            callDelete()
        })
        dialog.show()
    }

    private fun callDelete() {
        showProgress()
        var apiRequest = DeleteStduentReq(auth_token,scl_id,student_id,teacherId)
        Log.d("deleteStudentReq", apiRequest.toString())
        val call: Call<StudentDeleteResponse> = parentApiService!!.deleteStudent(apiRequest)
        call.enqueue(object : Callback<StudentDeleteResponse> {
            override fun onResponse(call: Call<StudentDeleteResponse>, response: Response<StudentDeleteResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@FillOtherDetailsActivity, FillSchoolDetailsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDeleteResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, t.message.toString())
            }
        })
    }


    private fun allCabs() {
        showProgress()
        var loginApiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<CabsListResponse> = parentApiService!!.allCabs(loginApiRequest)
        call.enqueue(object : Callback<CabsListResponse> {
            override fun onResponse(call: Call<CabsListResponse>, response: Response<CabsListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.cabs?.let {
                        hideProgress()
                        cabList.clear()
                        cabList.addAll(it)

                        groupsList()
                    }
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        groupsList()
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@FillOtherDetailsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@FillOtherDetailsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<CabsListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, t.message.toString())
            }
        })
    }

    private fun addPersons(nameEt: EditText, relationEt: EditText, bottomSheetDialog: BottomSheetDialog) {
        showProgress()
        var apiRequest = AddAuthorizedReq(auth_token,encodedPic.toString(),nameEt.text.toString(),
            scl_id,student_id,relationEt.text.toString(),teacherId,"add")
        val call: Call<SuccessResponsePojo> = parentApiService!!.addAuth(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        bottomSheetDialog.dismiss()
                        addPersonsList()
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, t.message.toString())
            }
        })
    }

    private fun addPersonsList() {
        showProgress()
        var apiRequest = AddAuthorizedReq(auth_token,"","", scl_id,student_id,"",teacherId,"view")
        val call: Call<SuccessResponsePojo> = parentApiService!!.addAuth(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

//                        val adapter = AuthPersonsAdapter(this@FillOtherDetailsActivity,response.response.consent_details ?: emptyList())
//                        binding.addPersonsRv.adapter = adapter
//                        var layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
//                        binding.addPersonsRv.layoutManager = layoutManager

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, t.message.toString())
            }
        })
    }


    private fun groupsList() {
        showProgress()
        var loginApiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("groupReq2025", loginApiRequest.toString())
        var call: Call<GroupsListResponse> = parentApiService!!.groupList(loginApiRequest)
        call.enqueue(object : Callback<GroupsListResponse> {
            override fun onResponse(call: Call<GroupsListResponse>, response: Response<GroupsListResponse>) {
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
                        ToastUtils.showSuccessCustomToast(this@FillOtherDetailsActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@FillOtherDetailsActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<GroupsListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, t.message.toString())
            }
        })
    }

    private fun selectCabs() {
        val classNamess = cabList.map { it.cab_name }.toTypedArray()

        val selectedSectionIndex = cabList.indexOfFirst { cabIds.contains(it.id) }

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Select Cab")
            .setSingleChoiceItems(classNamess, selectedSectionIndex) { _, which ->
                // Update the selected section based on user choice
                cabIds.clear()
                cabNames.clear()
                cabIds.add(cabList[which].id)
                cabNames.add(cabList[which].cab_name)
            }
            .setPositiveButton("OK") { _, _ ->
                // Update UI and log the selection
                selectedCab_ids = cabIds.joinToString("")
                selectedCab_names = cabNames.joinToString("")
                binding.selectedCabTv.text = selectedCab_names.toString()
                Log.d("selectedCabss", selectedCab_names.toString())

            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancel action if needed
                selectedCab_ids = ""
                selectedCab_names = ""
                binding.selectedCabTv.text = ""
                Log.d("SelectedSection", "Selection cancelled")
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun selectGroups() {
        val classNamess = groupList.map { it.group_name }.toTypedArray()

        val selectedSectionIndex = groupList.indexOfFirst { groupIds.contains(it.id) }

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Select Group")
            .setSingleChoiceItems(classNamess, selectedSectionIndex) { _, which ->
                // Update the selected section based on user choice
                groupIds.clear()
                groupNames.clear()
                groupIds.add(groupList[which].id)
                groupNames.add(groupList[which].group_name)
            }
            .setPositiveButton("OK") { _, _ ->
                // Update UI and log the selection
                selectedGroup_ids = groupIds.joinToString("")
                selectedGroup_names = groupNames.joinToString("")
                binding.selectedGroupTv.text = selectedGroup_names.toString()
                Log.d("selectedGroups", selectedGroup_names.toString())

            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancel action if needed
                selectedGroup_ids = ""
                selectedGroup_ids = ""
                binding.selectedGroupTv.text = ""
                Log.d("SelectedSection", "Selection cancelled")
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showbackDialog()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage(selectedImage: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val b = baos.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        encodedPic = encoder.encodeToString(b)

        return encodedPic
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this)
            if (items[item] == "Take Photo") {
                // userChoosenTask = "Take Photo"
                openCamera()
            } else if (items[item] == "Choose from Gallery") {
                //userChoosenTask = "Choose from Gallery"
                openGalleryy()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openGalleryy() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            resultLaunchergallery.launch(intent)
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this.let {
                intent.resolveActivity(it.packageManager)?.also {
                    resultLauncher.launch(intent)
                }
            }
        }
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

                        allCabs()

                        student_id = loginApiResponse.response.student_details.id.toString()

                        selectedCab_ids = loginApiResponse.response.student_details.cabs.toString()
                        selectedGroup_ids = loginApiResponse.response.student_details.groups.toString()

                        binding.amountEt.setText(loginApiResponse.response.student_details.security_amount.toString())
                        binding.selectedCabTv.text = loginApiResponse.response.student_details.cab_names.toString()
                        binding.selectedGroupTv.text = loginApiResponse.response.student_details.group_names.toString()

                    }else{
                        hideProgress()
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillOtherDetailsActivity, t.message.toString())
            }
        })
    }


}