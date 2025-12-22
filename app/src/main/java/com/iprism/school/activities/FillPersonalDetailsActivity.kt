package com.iprism.school.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityFillPersonalDetailsBinding
import com.iprism.school.model.Request.CreateStudentReq
import com.iprism.school.model.Request.EditStudentReq
import com.iprism.school.model.Request.StudentDetailsReq
import com.iprism.school.model.Response.CreateStudentResponse
import com.iprism.school.model.Response.StudentDetailsResponse
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Locale

class FillPersonalDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityFillPersonalDetailsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var admissionId: String = ""
    private var selected_session_ids: String = ""
    private var selected_class_ids: String = ""

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""

    private var gender_name: String? = ""
    private var bloodgroupName: String? = ""
    private var studentId: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        admissionId = intent.getStringExtra("admissionId").toString()
        selected_session_ids = intent.getStringExtra("selected_session_ids").toString()
        selected_class_ids = intent.getStringExtra("selected_class_ids").toString()

        if (tag == "edit"){
            studentId = intent.getStringExtra("studentId").toString()
            callStudentDetails()
        }else{

        }



        binding.addProfilePic.setOnClickListener {
            selectImage()
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
                binding.profileImg.setImageBitmap(bitmap)
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
                binding.profileImg.setImageBitmap(selectedImage)
                encodedPic = encodeImage(selectedImage)
            }
        }

        binding.dobLl.setOnClickListener {
            showDatePickerDialog()
        }

        binding.joiningLl.setOnClickListener {
            showDatePickerDialog2()
        }

        val genderoptions = arrayOf("Male", "Female", "Other")
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
                    gender_name = selectedOption.toString()
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

        val bloodoptions = arrayOf("A+", "A-", "B+","B-","AB+","AB-","O+","O-")
        binding.bloodll.setOnClickListener {
            // Track the selected option
            var selectedOption = ""
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an Option")
            builder.setSingleChoiceItems(bloodoptions, -1) { dialog, which ->
                selectedOption = bloodoptions[which] // Capture the selected option
            }
            builder.setPositiveButton("OK") { dialog, _ ->
                if (selectedOption.isNotEmpty()) {
                    bloodgroupName = selectedOption.toString()
                    binding.selectedbloodGroupTv.text = selectedOption.toString()
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

        binding.nextButton.setOnClickListener {
            hideKeyboard()
            if (tag == "edit"){
                if (binding.nameEt.text.toString() == ""||binding.nameEt.text.toString() == null){
                    showToast("Enter Full Name")
                }else if (binding.selectedDob.text.toString() == ""||binding.selectedDob.text.toString() == null){
                    showToast("Select Date of Birth")
                } else if (binding.selectedJoiningTv.text.toString() == ""||binding.selectedJoiningTv.text.toString() == null){
                    showToast("Select Joining Date")
                }else if (binding.selectedbloodGroupTv.text.toString()== ""||binding.selectedbloodGroupTv.text.toString()==null){
                    showToast("Select Blood Group")
                }else if (binding.selectedgenderTv.text.toString() == ""||binding.selectedgenderTv.text.toString() == null){
                    showToast("Select Gender")
                } else if (binding.castEt.text.toString() == ""||binding.castEt.text.toString() == null){
                    showToast("Enter Cast")
                } else if (binding.religionEt.text.toString() == ""||binding.religionEt.text.toString() == null){
                    showToast("Enter Religion")
                } else if (binding.nationalityEt.text.toString() == ""||binding.nationalityEt.text.toString() == null){
                    showToast("Enter Nationality")
                } else {
                    editStudent()
                }

            }else{

                if (encodedPic == ""||encodedPic == null){
                    showToast("Select Profile Pic")
                }else if (binding.nameEt.text.toString() == ""||binding.nameEt.text.toString() == null){
                        showToast("Enter Full Name")
                    }else if (binding.selectedDob.text.toString() == ""||binding.selectedDob.text.toString() == null){
                        showToast("Select Date of Birth")
                    } else if (binding.selectedJoiningTv.text.toString() == ""||binding.selectedJoiningTv.text.toString() == null){
                        showToast("Select Joining Date")
                    }else if (binding.selectedbloodGroupTv.text.toString()== ""||binding.selectedbloodGroupTv.text.toString()==null){
                        showToast("Select Blood Group")
                    }else if (binding.selectedgenderTv.text.toString() == ""||binding.selectedgenderTv.text.toString() == null){
                        showToast("Select Gender")
                    } else if (binding.castEt.text.toString() == ""||binding.castEt.text.toString() == null){
                        showToast("Enter Cast")
                    } else if (binding.religionEt.text.toString() == ""||binding.religionEt.text.toString() == null){
                        showToast("Enter Religion")
                    } else if (binding.nationalityEt.text.toString() == ""||binding.nationalityEt.text.toString() == null){
                        showToast("Enter Nationality")
                    } else {
                        createStudent()
                    }
            }
        }
    }

    private fun createStudent() {
        showProgress()
        var apiRequest = CreateStudentReq(admissionId,auth_token,binding.castEt.text.toString(),
            selected_class_ids,binding.selectedJoiningTv.text.toString()
            ,binding.nationalityEt.text.toString(),binding.religionEt.text.toString(),scl_id,selected_session_ids,
            binding.selectedbloodGroupTv.text.toString(),binding.selectedDob.text.toString()
            ,binding.selectedgenderTv.text.toString(),encodedPic.toString(),binding.nameEt.text.toString(),teacherId)
        Log.d("createStaffReq", apiRequest.toString())
        val call: Call<CreateStudentResponse> = parentApiService!!.createStudent(apiRequest)
        call.enqueue(object : Callback<CreateStudentResponse> {
            override fun onResponse(call: Call<CreateStudentResponse>, response: Response<CreateStudentResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        Log.d("student_id",loginApiResponse.response.student_id.toString())
                        val intent = Intent(this@FillPersonalDetailsActivity, FillParentDetailsActivity::class.java)
                        intent.putExtra("student_id",loginApiResponse.response.student_id.toString())
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillPersonalDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<CreateStudentResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillPersonalDetailsActivity, t.message.toString())
            }
        })
    }

    private fun editStudent() {
        showProgress()
        var apiRequest = EditStudentReq(admissionId,auth_token,binding.castEt.text.toString(),
            selected_class_ids,binding.selectedJoiningTv.text.toString()
            ,binding.nationalityEt.text.toString(),binding.religionEt.text.toString(),scl_id,selected_session_ids,
            binding.selectedbloodGroupTv.text.toString(),binding.selectedDob.text.toString()
            ,binding.selectedgenderTv.text.toString(),studentId.toString(),encodedPic.toString(),binding.nameEt.text.toString(),teacherId)
        Log.d("createStaffReq", apiRequest.toString())
        val call: Call<CreateStudentResponse> = parentApiService!!.editStudent(apiRequest)
        call.enqueue(object : Callback<CreateStudentResponse> {
            override fun onResponse(call: Call<CreateStudentResponse>, response: Response<CreateStudentResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        Log.d("student_id",loginApiResponse.response.student_id.toString())
                        val intent = Intent(this@FillPersonalDetailsActivity, FillParentDetailsActivity::class.java)
                        intent.putExtra("student_id",studentId)
                        intent.putExtra("tag",tag)
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillPersonalDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<CreateStudentResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillPersonalDetailsActivity, t.message.toString())
            }
        })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            Log.d("SelectedDate", "Selected date: $formattedDate")

            binding.selectedDob.text = formattedDate.toString()
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.YEAR, -18)

            if (selectedDate.before(currentDate)) {

            } else {
                Toast.makeText(this, "You must be 18 or older", Toast.LENGTH_SHORT).show()
            }

        }, year, month, day)
        calendar.add(Calendar.YEAR, -18)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun showDatePickerDialog2() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear)
                binding.selectedJoiningTv.text = formattedDate.toString()
            },
            year,
            month,
            day
        )

        // Restrict the calendar to prevent future dates
//        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
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
        val builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
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

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val date = Calendar.getInstance()
        date.set(year, month - 1, day)  // month is zero-based in Calendar
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date.time)
    }

    private fun callStudentDetails() {
        showProgress()
        var apiRequest = StudentDetailsReq(auth_token,scl_id,studentId.toString(),teacherId)
        Log.d("studentDetails2", apiRequest.toString())
        val call: Call<StudentDetailsResponse> = parentApiService!!.studentsDetails(apiRequest)
        call.enqueue(object : Callback<StudentDetailsResponse> {
            override fun onResponse(call: Call<StudentDetailsResponse>, response: Response<StudentDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        studentId = loginApiResponse.response.student_details.id.toString()

                        Glide.with(this@FillPersonalDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.student_details.student_image)
                            .into(binding.profileImg)

                        binding.nameEt.setText(loginApiResponse.response.student_details.student_name.toString())
                        binding.selectedDob.text = loginApiResponse.response.student_details.student_dob.toString()
                        binding.selectedJoiningTv.text = loginApiResponse.response.student_details.joining_date.toString()

                        binding.selectedbloodGroupTv.text = loginApiResponse.response.student_details.student_blood_group.toString()
                        binding.selectedgenderTv.text = loginApiResponse.response.student_details.student_gender.toString()

                        binding.castEt.setText(loginApiResponse.response.student_details.caste.toString())
                        binding.religionEt.setText(loginApiResponse.response.student_details.religion.toString())
                        binding.nationalityEt.setText(loginApiResponse.response.student_details.nationality.toString())

                    }else{
                        showToast(loginApiResponse.message.toString())
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FillPersonalDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FillPersonalDetailsActivity, t.message.toString())
            }
        })
    }

}