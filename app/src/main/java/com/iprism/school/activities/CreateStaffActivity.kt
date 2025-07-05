package com.iprism.school.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.ETC1.encodeImage
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.StaffActivity
import com.iprism.school.activities.calender.CreateCalenderActivity
import com.iprism.school.adapters.StaffListAdapter
import com.iprism.school.databinding.ActivityCreateStaffBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.GenderBottomSheetDialogBinding
import com.iprism.school.databinding.RightsBottomSheetBinding
import com.iprism.school.model.Request.AllclassesReq
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.CreateNewStaffReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.StaffDetailsReq
import com.iprism.school.model.Request.StaffDetailsUpdateReq
import com.iprism.school.model.Request.StaffListReq
import com.iprism.school.model.Request.StaffListResponse
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.AllClassesResponse
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ClasseAllList
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.model.Response.GenerateIdResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.StaffDetailsResponse
import com.iprism.school.model.Response.StudentList
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.Constants
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Locale

class CreateStaffActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateStaffBinding
    private var genderType: String = ""

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""
    private val CAMERA_PERMISSION_CODE = 100

    private var firstChar : String? = null

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseAllList>()

    private var selected_class_ids : String? = ""
    private var selected_class_names : String? = ""

    private val studentNames = mutableListOf<String>()
    private val studentIds = mutableListOf<String>()
    private val studentList = mutableListOf<StudentList>()

    private var selected_student_ids : String? = ""
    private var selected_student_names : String? = ""

    private var employeeId : String? = ""
    private var employee_use_designation : String? = "no"
    private var staffId : String? = ""
    private var status : String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()

        handleBack()
        handleGenderLo()

        callclasses()

        binding.checkBox5.setOnCheckedChangeListener { _, isChecked ->
            employee_use_designation = if (isChecked) "yes" else "no"
            Log.d("Checkbox Value", employee_use_designation.toString())
        }


        binding.generateIdBtn.setOnClickListener {
            generateId()
        }

        binding.profileImg.setOnClickListener {
            selectImage()
        }

        binding.dobLl.setOnClickListener {
            showDatePickerDialog()
        }

        binding.dojLl.setOnClickListener {
            showDatePickerDialog2()
        }

        binding.teachersLo.setOnClickListener {
            showClasses()
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

        binding.createUserBtn.setOnClickListener {
            hideKeyboard()
            val text = binding.mobileEt.text.toString()
            if (text == ""||text == null){

            }else{
                firstChar = text[0].toString()
            }

            if (tag == "edit"){

//                if (encodedPic == ""||encodedPic == null){
//                    showToast("Select Profile Pic")
//                }else

                    if (binding.nameEt.text.toString() == ""||binding.nameEt.text.toString() == null){
                    showToast("Enter Full Name")
                }else if (binding.selectedDob.text.toString() == ""||binding.selectedDob.text.toString() == null){
                    showToast("Select Date of Birth")
                }else if (binding.selectedGender.text.toString() == ""||binding.selectedGender.text.toString() == null||binding.selectedGender.text.toString() == "0"){
                    showToast("Select Gender")
                }else if ( binding.mobileEt.text.toString().length < 10 ){
                    Toast.makeText(this@CreateStaffActivity, "Mobile Number should be 10 Digits", Toast.LENGTH_SHORT).show()
                }else if (binding.mobileEt.text.toString() == "0000000000"){
                    Toast.makeText(this@CreateStaffActivity, "Enter valid Mobile Number", Toast.LENGTH_SHORT).show()
                }else if (firstChar == ""||firstChar == null||firstChar == "1"||firstChar == "2"||firstChar == "3"||firstChar == "4"||firstChar == "5"||firstChar == "0"){
                    Toast.makeText(this@CreateStaffActivity,"Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
                } else if ( !isValidMobile(binding.mobileEt.text.toString())){
                    Toast.makeText(this@CreateStaffActivity, "Enter valid Mobile Number", Toast.LENGTH_SHORT).show()
                }else if (binding.emailEt.text.toString() == ""||binding.emailEt.text.toString() == null){
                    showToast("Enter Email Id")
                }

//                    else if (binding.passwordEt.text.toString()== ""||binding.passwordEt.text.toString()==null){
//                    showToast("Enter Password")
//                }else if (binding.passwordEt.text.toString()== ""||binding.passwordEt.text.toString()==null){
//                    showToast("Enter confirm password")
//                }

                    else if (binding.emplyeeId.text.toString() == ""||binding.emplyeeId.text.toString() == null){
                    showToast("Generate Employee Id")
                }else if (binding.designationEt.text.toString()== ""||binding.designationEt.text.toString()==null){
                    showToast("Enter Designation")
                }else if (binding.dateOfJoiningTxt.text.toString() == ""||binding.dateOfJoiningTxt.text.toString() == null){
                    showToast("Enter Date of Joining")
                }
//            else if (selected_student_ids == ""||selected_student_ids == null){
//                showToast("Select Access Rights")
//            }
                else if (selected_class_ids == ""||selected_class_ids == null){
                    showToast("Select Class")
                } else {
                    callEditStaff()
                }

            }else{

                if (encodedPic == ""||encodedPic == null){
                    showToast("Select Profile Pic")
                }else if (binding.nameEt.text.toString() == ""||binding.nameEt.text.toString() == null){
                    showToast("Enter Full Name")
                }else if (binding.selectedDob.text.toString() == ""||binding.selectedDob.text.toString() == null){
                    showToast("Select Date of Birth")
                }else if (binding.selectedGender.text.toString() == ""||binding.selectedGender.text.toString() == null||binding.selectedGender.text.toString() == "0"){
                    showToast("Select Gender")
                }else if ( binding.mobileEt.text.toString().length < 10 ){
                    Toast.makeText(this@CreateStaffActivity, "Mobile Number should be 10 Digits", Toast.LENGTH_SHORT).show()
                }else if (binding.mobileEt.text.toString() == "0000000000"){
                    Toast.makeText(this@CreateStaffActivity, "Enter valid Mobile Number", Toast.LENGTH_SHORT).show()
                }else if (firstChar == ""||firstChar == null||firstChar == "1"||firstChar == "2"||firstChar == "3"||firstChar == "4"||firstChar == "5"||firstChar == "0"){
                    Toast.makeText(this@CreateStaffActivity,"Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
                } else if ( !isValidMobile(binding.mobileEt.text.toString())){
                    Toast.makeText(this@CreateStaffActivity, "Enter valid Mobile Number", Toast.LENGTH_SHORT).show()
                }else if (binding.emailEt.text.toString() == ""||binding.emailEt.text.toString() == null){
                    showToast("Enter Email Id")
                }

//                else if (binding.passwordEt.text.toString()== ""||binding.passwordEt.text.toString()==null){
//                    showToast("Enter Password")
//                }else if (binding.passwordEt.text.toString()== ""||binding.passwordEt.text.toString()==null){
//                    showToast("Enter confirm password")
//                }

                else if (binding.emplyeeId.text.toString() == ""||binding.emplyeeId.text.toString() == null){
                    showToast("Generate Employee Id")
                }else if (binding.designationEt.text.toString()== ""||binding.designationEt.text.toString()==null){
                    showToast("Enter Designation")
                }else if (binding.dateOfJoiningTxt.text.toString() == ""||binding.dateOfJoiningTxt.text.toString() == null){
                    showToast("Enter Date of Joining")
                }
//            else if (selected_student_ids == ""||selected_student_ids == null){
//                showToast("Select Access Rights")
//            }
                else if (selected_class_ids == ""||selected_class_ids == null){
                    showToast("Select Class")
                } else {
                    callCreateAndEditStaff()
                }
            }



        }
    }

    private fun callEditView() {
        showProgress()
        var apiRequest = StaffDetailsReq(auth_token,scl_id,staffId.toString(),status.toString(),teacherId)
        Log.d("staffDetailsReq", apiRequest.toString())
        val call: Call<StaffDetailsResponse> = parentApiService!!.singleStaffDetails(apiRequest)
        call.enqueue(object : Callback<StaffDetailsResponse> {
            override fun onResponse(call: Call<StaffDetailsResponse>, response: Response<StaffDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        Glide.with(this@CreateStaffActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.staff[0].employee_image)
                            .into(binding.profileImg)

                        binding.nameEt.setText(loginApiResponse.response.staff[0].employee_name.toString())
                        binding.selectedDob.setText(loginApiResponse.response.staff[0].employee_dob.toString())
                        binding.selectedGender.setText(loginApiResponse.response.staff[0].employee_gender.toString())
                        binding.mobileEt.setText(loginApiResponse.response.staff[0].employee_mobile.toString())
                        binding.emailEt.setText(loginApiResponse.response.staff[0].employee_email.toString())
                        binding.passwordEt.setText(loginApiResponse.response.staff[0].employee_password.toString())
                        binding.confirmPasswordEt.setText(loginApiResponse.response.staff[0].employee_password.toString())
                        binding.emplyeeId.setText(loginApiResponse.response.staff[0].employee_id.toString())
                        binding.designationEt.setText(loginApiResponse.response.staff[0].employee_designation.toString())
                        binding.dateOfJoiningTxt.setText(loginApiResponse.response.staff[0].date_of_joining.toString())
                        binding.departmentEt.setText(loginApiResponse.response.staff[0].employee_department.toString())
//                        binding.accesRights.setText(loginApiResponse.response.staff[0].ri.toString())
                        binding.selectedClass.setText(loginApiResponse.response.staff[0].class_names.toString())

                        selected_class_ids = loginApiResponse.response.staff[0].employee_class.toString()

                        val value = loginApiResponse.response.staff[0].employee_use_designation.toString()

                        if (value == "yes"){
                            binding.checkBox5.isChecked = value == "yes"
                        }

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateStaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StaffDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateStaffActivity, t.message.toString())
            }
        })
    }


    private fun callCreateAndEditStaff() {
        showProgress()
        var apiRequest = CreateNewStaffReq("", auth_token,binding.dateOfJoiningTxt.text.toString(),selected_student_ids.toString()
            ,binding.departmentEt.text.toString(),binding.designationEt.text.toString(),binding.selectedDob.text.toString(),
            binding.emailEt.text.toString(),binding.selectedGender.text.toString(),binding.emplyeeId.text.toString(),
            encodedPic.toString(),binding.mobileEt.text.toString(),binding.nameEt.text.toString()
            ,employee_use_designation.toString(),scl_id,teacherId)
        Log.d("createStaffReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.createStaff(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@CreateStaffActivity, StaffActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateStaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateStaffActivity, t.message.toString())
            }
        })
    }


    private fun callEditStaff() {
        showProgress()
        var apiRequest = StaffDetailsUpdateReq(auth_token,
            binding.dateOfJoiningTxt.text.toString()
            ,selected_class_ids.toString()
            ,binding.departmentEt.text.toString()
            ,binding.designationEt.text.toString()
            ,binding.selectedDob.text.toString(),
            binding.emailEt.text.toString() ,binding.selectedGender.text.toString()
            ,binding.emplyeeId.text.toString(),
            encodedPic.toString(),binding.mobileEt.text.toString(),binding.nameEt.text.toString()
            ,employee_use_designation.toString(),scl_id,staffId.toString(),teacherId)
        Log.d("createStaffReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.updateStaffDetails(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@CreateStaffActivity, StaffActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateStaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateStaffActivity, t.message.toString())
            }
        })
    }

    private fun handleGenderLo() {
        binding.genderLo.setOnClickListener(View.OnClickListener {
            showGenderBottomSheet()
        })
    }

    private fun showGenderBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomBinding = GenderBottomSheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            setupCheckboxes(bottomBinding.maleCb, bottomBinding.femaleCb)
            bottomBinding.confirmBtn.setOnClickListener(View.OnClickListener {
                if (genderType.equals("")) {
                    ToastUtils.showErrorCustomToast(this, "Please Select gender Type")
                } else {
                    bottomSheetDialog.dismiss()
                    binding.selectedGender.text = genderType
                    ToastUtils.showSuccessCustomToast(this, genderType)
                }
            })

            bottomBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            bottomBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showAddRightsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val rightsBinding = RightsBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(rightsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            rightsBinding.confirmBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Rights Added Successfully")
            })

            rightsBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            rightsBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
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
                // Format and display the selected date in the EditText
//                val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
//                binding.etDateofjoining.text = formattedDate
                val formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear)
                binding.dateOfJoiningTxt.text = formattedDate.toString()

            },
            year,
            month,
            day
        )

        // Restrict the calendar to prevent future dates
//        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun setupCheckboxes(checkBox1: CheckBox, checkBox2: CheckBox) {
        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox2.isChecked = false
                genderType = "Male"
            } else if (!checkBox2.isChecked) {
                genderType = ""
            }
        }

        checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false
                genderType = "Female"
            } else if (!checkBox1.isChecked) {
                genderType = ""
            }
        }
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

                        if (tag == "edit"){

                            binding.textView10.text = "Update Staff"

                            staffId = intent.getStringExtra("staffId").toString()
                            status = intent.getStringExtra("status").toString()
                            callEditView()
                        }else{
                            binding.textView10.text = "Create Staff"
                        }

                    }
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateStaffActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateStaffActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateStaffActivity, response.message())
                }
            }

            override fun onFailure(call: Call<AllClassesResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateStaffActivity, t.message.toString())
            }
        })
    }


    private fun callStudents() {
        showProgress()
        var loginApiRequest = CLass_StudentsReq( auth_token,selected_class_ids.toString(),"active",teacherId )
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<Class_studentResponse> = parentApiService!!.teacherStudents(loginApiRequest)
        call.enqueue(object : Callback<Class_studentResponse> {
            override fun onResponse(call: Call<Class_studentResponse>, response: Response<Class_studentResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.students?.let {
                        hideProgress()
                        studentList.clear()
                        studentList.addAll(it)
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@CreateStaffActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@CreateStaffActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateStaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<Class_studentResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateStaffActivity, t.message.toString())
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
                selected_class_names = classNames.joinToString(" , ")
                binding.selectedClass.text = selected_class_names
                Log.d("SelectedClass", selected_class_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                classNames.clear()
                classIds.clear()
                selected_class_ids = ""
                selected_class_names = ""
                binding.selectedClass.text = ""

                studentNames.clear()
                studentIds.clear()
                selected_student_names = ""
                selected_student_ids = ""
//                binding.selectedstudent.text = ""

            }
            .create()
        dialog.show()
    }

    private fun showstudents() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select Students"

        val originalClassNames = mutableListOf("Select All") + studentList.map { it.student_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = studentNames.toMutableSet()
        val tempClassIds = studentIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        studentList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == studentList.size) {
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
                    tempClassNames.addAll(studentList.map { it.student_name })
                    tempClassIds.addAll(studentList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = studentList[which - 1].student_name
                val selectedClassId = studentList[which - 1].id

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
                checkedItems[0] = tempClassNames.size == studentList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(studentList.map { it.student_name })
                } else {
                    filteredClassNames.addAll(studentList.filter { it.student_name.contains(newText, true) }.map { it.student_name })
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
                studentNames.clear()
                studentIds.clear()
                studentNames.addAll(tempClassNames)
                studentIds.addAll(tempClassIds)

                selected_student_ids = studentIds.joinToString(",")
                selected_student_names = studentNames.joinToString(" , ")
//                binding.selectedstudent.text = selected_student_names
                Log.d("selectedStudents", selected_student_ids.toString())

            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                studentNames.clear()
                studentIds.clear()
                selected_student_names = ""
                selected_student_ids = ""
//                binding.selectedstudent.text = ""
            }
            .create()
        dialog.show()
    }

    private fun generateId() {
        showProgress()
        var apiRequest =SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("staffList", apiRequest.toString())
        val call: Call<GenerateIdResponse> = parentApiService!!.generateId(apiRequest)
        call.enqueue(object : Callback<GenerateIdResponse> {
            override fun onResponse(call: Call<GenerateIdResponse>, response: Response<GenerateIdResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        employeeId = loginApiResponse.response.admission_id.toString()

                        binding.emplyeeId.text = employeeId.toString()

                    }else{
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateStaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GenerateIdResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateStaffActivity, t.message.toString())
            }
        })
    }

    private fun isValidMobile(toString: String): Boolean {
        return Patterns.PHONE.matcher(toString).matches()
    }

}