package com.iprism.school.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.FillPersonalDetailsActivity
import com.iprism.school.databinding.ActivityFillParentDetailsBinding
import com.iprism.school.databinding.ActivityFillPersonalDetailsBinding
import com.iprism.school.model.Request.CreateStudentReq
import com.iprism.school.model.Request.DeleteStduentReq
import com.iprism.school.model.Request.ParentStudentReq
import com.iprism.school.model.Request.StudentDetailsReq
import com.iprism.school.model.Response.CreateStudentResponse
import com.iprism.school.model.Response.ParentDetailsResponse
import com.iprism.school.model.Response.StudentDeleteResponse
import com.iprism.school.model.Response.StudentDetailsResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64

class FillParentDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityFillParentDetailsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var student_id: String = ""

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    private var encodedPic1: String? = ""
    private var encodedPic2: String? = ""
    private var pic_click: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillParentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        student_id = intent.getStringExtra("student_id").toString()

        handleBack()
        handleNext()

        if (tag == "edit"){
            callStudentDetails()
        }

        binding.addfatherPic.setOnClickListener {
            selectImage()
            pic_click = "1"
        }

        binding.addmotherPic.setOnClickListener {
            selectImage()
            pic_click = "2"
        }


        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val bitmap = data?.extras?.get("data") as Bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b = baos.toByteArray()
                    val encoder: Base64.Encoder = Base64.getEncoder()
                    if (pic_click == "1") {
                        binding.fatherPic.setImageBitmap(bitmap)
                        encodedPic1 = encoder.encodeToString(b)
                    } else if (pic_click == "2") {
                        binding.motherPic.setImageBitmap(bitmap)
                        encodedPic2 = encoder.encodeToString(b)
                    }
                }
            }

        resultLaunchergallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val uri = data?.data
                    val imagestrem: InputStream? = this.contentResolver?.openInputStream(uri!!)
                    val selectedImage: Bitmap = BitmapFactory.decodeStream(imagestrem)

                    if (pic_click == "1") {
                        binding.fatherPic.setImageBitmap(selectedImage)
                        encodedPic1 = encodeImage1(selectedImage)
                    } else if (pic_click == "2") {
                        binding.motherPic.setImageBitmap(selectedImage)
                        encodedPic2 = encodeImage2(selectedImage)
                    }
                }
            }

        binding.nextButton.setOnClickListener {
            if (tag == "edit") {
                if (binding.nameEt.text.toString() == "" || binding.nameEt.text.toString() == null) {
                    showToast("Enter Full Name")
                } else if (binding.fMobileEt.text.toString() == "" || binding.fMobileEt.text.toString() == null) {
                    showToast("Enter father mobile Number")
                } else if (binding.femailEt.text.toString() == "" || binding.femailEt.text.toString() == null) {
                    showToast("Enter  father email")
                } else if (binding.fOccupationEt.text.toString() == "" || binding.fOccupationEt.text.toString() == null) {
                    showToast("Enter father occupation")
                } else if (binding.motherNameEt.text.toString() == "" || binding.motherNameEt.text.toString() == null) {
                    showToast("Enter mother name")
                } else if (binding.mothermobileEt.text.toString() == "" || binding.mothermobileEt.text.toString() == null) {
                    showToast("Enter mother mobile number")
                } else if (binding.motheremailEt.text.toString() == "" || binding.motheremailEt.text.toString() == null) {
                    showToast("Enter mother email")
                } else if (binding.motheroccupationEt.text.toString() == "" || binding.motheroccupationEt.text.toString() == null) {
                    showToast("Enter mother occupation")
                }else if (binding.gurdnameEt.text.toString() == "" || binding.gurdnameEt.text.toString() == null) {
                    showToast("Enter Guardian Name")
                }else if (binding.gurdmobileEt.text.toString() == "" || binding.gurdmobileEt.text.toString() == null) {
                    showToast("Enter Guardian Mobile")
                }else if (binding.addressEt.text.toString() == "" || binding.addressEt.text.toString() == null) {
                    showToast("Enter Address")
                } else if (binding.pincodeEt.text.toString() == "" || binding.pincodeEt.text.toString() == null) {
                    showToast("Enter pin code")
                } else {
                    parentDetails()
                }

            } else {

                if (encodedPic1 == ""||encodedPic1 == null){
                    showToast("Select Father Pic")
                }else if (encodedPic2 == ""||encodedPic2 == null){
                    showToast("Select Mother Pic")
                }else if (binding.nameEt.text.toString() == "" || binding.nameEt.text.toString() == null) {
                    showToast("Enter Full Name")
                } else if (binding.fMobileEt.text.toString() == "" || binding.fMobileEt.text.toString() == null) {
                    showToast("Enter father mobile Number")
                } else if (binding.femailEt.text.toString() == "" || binding.femailEt.text.toString() == null) {
                    showToast("Enter  father email")
                } else if (binding.fOccupationEt.text.toString() == "" || binding.fOccupationEt.text.toString() == null) {
                    showToast("Enter father occupation")
                } else if (binding.motherNameEt.text.toString() == "" || binding.motherNameEt.text.toString() == null) {
                    showToast("Enter mother name")
                } else if (binding.mothermobileEt.text.toString() == "" || binding.mothermobileEt.text.toString() == null) {
                    showToast("Enter mother mobile number")
                } else if (binding.motheremailEt.text.toString() == "" || binding.motheremailEt.text.toString() == null) {
                    showToast("Enter mother email")
                } else if (binding.motheroccupationEt.text.toString() == "" || binding.motheroccupationEt.text.toString() == null) {
                    showToast("Enter mother occupation")
                }else if (binding.gurdnameEt.text.toString() == "" || binding.gurdnameEt.text.toString() == null) {
                    showToast("Enter Guardian Name")
                }else if (binding.gurdmobileEt.text.toString() == "" || binding.gurdmobileEt.text.toString() == null) {
                    showToast("Enter Guardian Mobile")
                }else if (binding.addressEt.text.toString() == "" || binding.addressEt.text.toString() == null) {
                    showToast("Enter Address")
                } else if (binding.pincodeEt.text.toString() == "" || binding.pincodeEt.text.toString() == null) {
                    showToast("Enter pin code")
                } else {
                    parentDetails()
                }
            }

        }
    }

    private fun parentDetails() {
        showProgress()
        var apiRequest = ParentStudentReq(binding.addressEt.text.toString()
            ,auth_token,binding.femailEt.text.toString()
            ,encodedPic1.toString(),binding.fMobileEt.text.toString()
            ,binding.nameEt.text.toString(),binding.fOccupationEt.text.toString()
            ,binding.gurdmobileEt.text.toString(),binding.gurdnameEt.text.toString(), binding.motheremailEt.text.toString(),
            encodedPic2.toString(),binding.mothermobileEt.text.toString(),binding.motherNameEt.text.toString()
            ,binding.motheroccupationEt.text.toString()
            ,binding.pincodeEt.text.toString(),scl_id,student_id,teacherId)
            val call: Call<ParentDetailsResponse> = parentApiService!!.parentStudent(apiRequest)
            call.enqueue(object : Callback<ParentDetailsResponse> {
            override fun onResponse(call: Call<ParentDetailsResponse>, response: Response<ParentDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@FillParentDetailsActivity, FillOtherDetailsActivity::class.java)
                        intent.putExtra("student_id",student_id.toString())
                        intent.putExtra("tag",tag.toString())
                        startActivity(intent)
                        finish()
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillParentDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<ParentDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillParentDetailsActivity, t.message.toString())
            }
        })
    }

    private fun handleNext() {
        binding.nextButton.setOnClickListener(View.OnClickListener {
            var  intent  = Intent(this, FillOtherDetailsActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            showbackDialog()
        })
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

                        val intent = Intent(this@FillParentDetailsActivity, FillSchoolDetailsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillParentDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDeleteResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillParentDetailsActivity, t.message.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage1(selectedImage: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val b = baos.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        encodedPic1 = encoder.encodeToString(b)

        return encodedPic1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage2(selectedImage: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val b = baos.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        encodedPic2 = encoder.encodeToString(b)
        return encodedPic2
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
                openGallery()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this?.let {
                intent.resolveActivity(it.packageManager)?.also {
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            resultLaunchergallery.launch(intent)
        }
    }

    override fun onBackPressed() {
        showbackDialog()
    }


    private fun callStudentDetails() {
        showProgress()
        var apiRequest = StudentDetailsReq(auth_token,scl_id,student_id.toString(),teacherId)
        Log.d("studentDetails3", apiRequest.toString())
        val call: Call<StudentDetailsResponse> = parentApiService!!.studentsDetails(apiRequest)
        call.enqueue(object : Callback<StudentDetailsResponse> {
            override fun onResponse(call: Call<StudentDetailsResponse>, response: Response<StudentDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        student_id = loginApiResponse.response.student_details.id.toString()

                        Glide.with(this@FillParentDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.student_details.father_image)
                            .into(binding.fatherPic)

                        Glide.with(this@FillParentDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.student_details.mother_image)
                            .into(binding.motherPic)

                        binding.nameEt.setText(loginApiResponse.response.student_details.student_name.toString())
                        binding.fMobileEt.setText(loginApiResponse.response.student_details.father_mobile.toString())
                        binding.femailEt.setText(loginApiResponse.response.student_details.father_email.toString())
                        binding.fOccupationEt.setText(loginApiResponse.response.student_details.father_occupation.toString())
                        binding.motherNameEt.setText(loginApiResponse.response.student_details.mother_name.toString())
                        binding.mothermobileEt.setText(loginApiResponse.response.student_details.mother_mobile.toString())
                        binding.motheremailEt.setText(loginApiResponse.response.student_details.mother_email.toString())
                        binding.motheroccupationEt.setText(loginApiResponse.response.student_details.mother_occupation.toString())
                        binding.gurdnameEt.setText(loginApiResponse.response.student_details.guardian_name.toString())
                        binding.gurdmobileEt.setText(loginApiResponse.response.student_details.guardian_mobile.toString())
                        binding.addressEt.setText(loginApiResponse.response.student_details.address.toString())
                        binding.pincodeEt.setText(loginApiResponse.response.student_details.pincode.toString())

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillParentDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillParentDetailsActivity, t.message.toString())
            }
        })
    }

}