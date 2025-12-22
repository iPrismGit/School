package com.iprism.school.activities.calender

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.CalenderImagesAdapter
import com.iprism.school.databinding.ActivityEditCalenderBinding
import com.iprism.school.databinding.FullViewImgBinding
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.CalenderImgDeleteReq
import com.iprism.school.model.Request.CalenderImgUpdateReq
import com.iprism.school.model.Request.CalenderUpdateReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Request.TeacherCalederDetailsReq
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.GroupsTeacher
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.Constants
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import com.iprism.school.viewModels.Scl_ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Locale


class EditCalenderActivity : BaseActivity() {

    private lateinit var binding: ActivityEditCalenderBinding
    private lateinit var binding3 : FullViewImgBinding
    private val viewModel: Scl_ViewModel by viewModels()
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseList>()

    private val studentNames = mutableListOf<String>()
    private val studentIds = mutableListOf<String>()
    private val studentList = mutableListOf<StudentList>()

    private val usersNames = mutableListOf<String>()
    private val usersIds = mutableListOf<String>()
    private val usersList = mutableListOf<StaffDetailList>()

    private val groupNames = mutableListOf<String>()
    private val groupIds = mutableListOf<String>()
    private val groupList = mutableListOf<GroupsTeacher>()

    private var selected_class_ids : String? = ""
    private var selected_class_names : String? = ""

    private var selected_student_ids : String? = ""
    private var selected_student_names : String? = ""

    private var selected_users_ids : String? = ""
    private var selected_users_names : String? = ""

    private var selected_group_ids : String? = ""
    private var selected_group_names : String? = ""

    private var advancedOptions : String? = ""

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""
    private var imagesCount: String? = null
    private lateinit var crossImage: ImageView
    private var calenderId: String = ""
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        calenderId = intent.getStringExtra("calenderId").toString()

        handleBack()
//        handleLink()
        handleLocationIv()
        handleDateLo()
        handleTimeLo()

        calenderDetails()

        binding.advancedLl.setOnClickListener(View.OnClickListener {
            showAdvancedOptionsBottomSheet()
        })

        binding.linkIv.setOnClickListener(View.OnClickListener {
            if (imagesCount.toString() >= 3.toString()||imagesCount == "3"){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Max Images 3 only")
            }else{
                openGallery()
//                selectImage()
            }
        })

        binding.addPersonIv.setOnClickListener {
            callGroups()
            binding.groupsLl.visibility = View.VISIBLE
        }

        binding.classLl.setOnClickListener {
            showClasses()
        }

        binding.studentLl.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Class")
            }else{
                showstudents()
            }
        }

        binding.usersLl.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Class")
            }else if (selected_student_ids == ""||selected_student_ids == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Students")
            } else{
                showusers()
            }
        }

        binding.groupsLl.setOnClickListener {
            showGroups()
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
//                binding.fatherimg.setImageBitmap(bitmap)
                encodedPic = encoder.encodeToString(b)
            }
        }

        resultLaunchergallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val uri = data?.data
                val imagestrem : InputStream? = this.contentResolver?.openInputStream(uri!!)
                val selectedImage  : Bitmap = BitmapFactory.decodeStream(imagestrem)
//                binding.fatherimg.setImageBitmap(selectedImage)
                encodedPic = encodeImage(selectedImage)

                var apiRequest = CalenderImgUpdateReq(encodedPic!!,"image",auth_token,calenderId,
                    scl_id,teacherId)
                updateImgCalender(apiRequest)
            }
        }

        binding.updateBtn.setOnClickListener(View.OnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Class")
            }else if (selected_student_ids == ""||selected_student_ids == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Students")
            }else if (selected_users_ids == ""||selected_users_ids == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Users")
            }else if (binding.subjectsEt.text.toString() == ""||binding.subjectsEt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Enter Subject")
            } else if (binding.dateTxt.text.toString() == ""||binding.dateTxt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Date")
            }else if (binding.timeTxt.text.toString() == ""||binding.timeTxt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Select Time")
            }else if (binding.detailsTxt.text.toString() == ""||binding.detailsTxt.text.toString() == null){
                ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, "Enter Details")
            }else{

                var apiRequest = CalenderUpdateReq(advancedOptions.toString(),"","",auth_token,
                    selected_class_ids.toString(),binding.dateTxt.text.toString(),
                    binding.detailsTxt.text.toString(),
                    selected_group_ids.toString(),binding.locationTxt.text.toString(),
                    scl_id,selected_users_ids.toString(),selected_student_ids.toString(),
                    binding.subjectsEt.text.toString(),teacherId,binding.timeTxt.text.toString(),calenderId)

                updateCalender(apiRequest)
            }

//            ToastUtils.showSuccessCustomToast(this, "Event Updated Successfully")
//            finish()
        })
    }

    private fun updateCalender(apiRequest: CalenderUpdateReq) {
        showProgress()
        Log.d("updateCalenderReq", apiRequest.toString())
        viewModel.calenderUpdate(apiRequest).observe(this@EditCalenderActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                Log.d("updateCalenderResponse", response.toString())
                startActivity(Intent(this@EditCalenderActivity, CalenderActivity::class.java))
                finish()
            } else {
                hideProgress()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                    finish()
                }
            }
        })
    }


    private fun updateImgCalender(apiRequest:CalenderImgUpdateReq) {
        showProgress()
        Log.d("updateImgCalenderReq", apiRequest.toString())
        viewModel.calenderImgUpdate(apiRequest).observe(this@EditCalenderActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()

                calenderDetails()

                binding.scrollView.scrollTo(0,0)

//                Log.d("updateCalenderResponse", response.toString())
//                startActivity(Intent(this@EditCalenderActivity, CalenderActivity::class.java))
//                finish()
            } else {
                hideProgress()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                    finish()
                }
            }
        })
    }

    private fun calenderDetails() {
        showProgress()
        var apiRequest = TeacherCalederDetailsReq(auth_token,calenderId,scl_id,teacherId)
        Log.d("calenderDetailsReq", apiRequest.toString())
        viewModel.teacherCalenderDetials(apiRequest).observe(this@EditCalenderActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                callclasses()
                selected_class_ids = response.response.calender_details[0].class_ids.toString()
                selected_student_ids = response.response.calender_details[0].student_ids.toString()
                selected_users_ids = response.response.calender_details[0].staff_ids.toString()
                selected_group_ids = response.response.calender_details[0].group_ids.toString()

                imagesCount = response.response.attachments.size.toString()
                Log.d("imagesCount",imagesCount.toString())

                val date = response.response.calender_details[0].calender_date.toString()
                val day = response.response.calender_details[0].day.toString()

                binding.dateTxt.text = convertDateFormat(date)

                binding.subjectsEt.setText(response.response.calender_details[0].subject.toString())
                binding.selectedclass.text = response.response.calender_details[0].class_names.toString()
                binding.selectedusers.text = response.response.calender_details[0].staff_names.toString()
//                binding.dateTxt.text =  date
                binding.timeTxt.text = response.response.calender_details[0].time.toString()
                binding.selectedclass.text = response.response.calender_details[0].class_names.toString()
                binding.selectedstudent.text = response.response.calender_details[0].student_names.toString()
                binding.selectedgroups.text = response.response.calender_details[0].group_names.toString()
                binding.advancedTV.text = response.response.calender_details[0].advance_options.toString()
                advancedOptions = response.response.calender_details[0].advance_options.toString()
                binding.locationTxt.setText( response.response.calender_details[0].location.toString())
                binding.detailsTxt.setText( response.response.calender_details[0].details.toString())

//              val adapter = CalenderImagesAdapter(this,response.response.attachments,null)
//                binding.imagesRv.adapter = adapter
//                var layoutManager = LinearLayoutManager(this@EditCalenderActivity, LinearLayoutManager.HORIZONTAL, false)
//                binding.imagesRv.layoutManager = layoutManager

                binding.imagesRv.layoutManager = GridLayoutManager(this,3)
                val adapter = CalenderImagesAdapter(this@EditCalenderActivity,response.response.attachments,null)
                binding.imagesRv.adapter = adapter
                adapter.notifyDataSetChanged()

                adapter.OnItemCallPic = {
                        mydata ->
                    val images = Constants.IMAGES_URL+mydata.attachment.toString()
                    Log.d("images2025",images.toString())
                    showFullView(images)
                }


                adapter.OnItemCallDelete = {
                        mydata ->
                    val picId = mydata.id.toString()
                    callDeleteImg(picId)
                }

            } else {
                hideProgress()
                callclasses()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                    finish()
                }else{

                }
            }
        })
    }

    private fun callDeleteImg(picId: String) {
        showProgress()
        var apiRequest = CalenderImgDeleteReq(picId,auth_token,calenderId,scl_id,teacherId)
        Log.d("calenderDeleteReq", apiRequest.toString())
        viewModel.calenderImgDelete(apiRequest).observe(this@EditCalenderActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                calenderDetails()
                binding.scrollView.scrollTo(0,0)

            } else {
                hideProgress()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                    finish()
                }else{

                }
            }
        })
    }

    private fun showFullView(imagesList: String) {
        val dialog = Dialog(this, android.R.style.Theme_Material_Dialog_Alert)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding3 = FullViewImgBinding.inflate(layoutInflater)
        dialog.setContentView(binding3.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        dialog.window!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.BOTTOM
        dialog.show()
        dialog.window!!.attributes = lp

        Glide.with(this)
            .load(imagesList)
            .into(binding3.imgView)


        binding3.closeImg.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun handleTimeLo() {
        binding.timeTxt.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, false)
        })
    }

    private fun handleLocationIv() {
        binding.addLocationIv.setOnClickListener(View.OnClickListener {
            binding.locationTxt.visibility = View.VISIBLE
        })
    }

//    private fun handleLink() {
//        binding.linkIv.setOnClickListener(View.OnClickListener {
//            showOptionsDialog()
//        })
//    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showOptionsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.camera_dilog_box, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.show()
    }

    private fun showAdvancedOptionsBottomSheet() {
        val advancedoptions = arrayOf("Do not repeat", "Weakly", "Monthly","Every 3 Months","Every 6 Months","Yearly")
        var selectedOption = ""
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Choose an Option")
        builder.setSingleChoiceItems(advancedoptions, -1) { dialog, which ->
            selectedOption = advancedoptions[which] // Capture the selected option
        }
        builder.setPositiveButton("OK") { dialog, _ ->
            if (selectedOption.isNotEmpty()) {
                advancedOptions = selectedOption.toString()
                binding.advancedTV.text = selectedOption.toString()
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage(selectedImage: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val b = baos.toByteArray()
        val encoder: Base64.Encoder = Base64.getEncoder()
        encodedPic = encoder.encodeToString(b)

        return encodedPic
    }

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
                intent.resolveActivity(this.packageManager)?.also {
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            this?.let {
                intent.resolveActivity(this.packageManager)?.also {
                    resultLaunchergallery.launch(intent)
                }
            }
        }
    }


    private fun callclasses() {
        showProgress()
        var loginApiRequest = TeacherAccessReq( teacherId,auth_token )
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

                        callStudents()
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@EditCalenderActivity, response.message())
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@EditCalenderActivity, t.message.toString())
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
                        callUsers()
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@EditCalenderActivity, response.message())
                }
            }
            override fun onFailure(call: Call<Class_studentResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@EditCalenderActivity, t.message.toString())
            }
        })
    }

    private fun callUsers() {
        showProgress()
        var loginApiRequest = SchoolStaffReq( auth_token,scl_id,teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<SchoolStaffResponse> = parentApiService!!.schoolStaff(loginApiRequest)
        call.enqueue(object : Callback<SchoolStaffResponse> {
            override fun onResponse(call: Call<SchoolStaffResponse>, response: Response<SchoolStaffResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.response?.staff_details?.let {
                        hideProgress()
                        usersList.clear()
                        usersList.addAll(it)
                        callGroups()
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        callGroups()
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@EditCalenderActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SchoolStaffResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@EditCalenderActivity, t.message.toString())
            }
        })
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
                        ToastUtils.showSuccessCustomToast(this@EditCalenderActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@EditCalenderActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@EditCalenderActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@EditCalenderActivity, t.message.toString())
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
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                classNames.clear()
                classIds.clear()
                classNames.addAll(tempClassNames)
                classIds.addAll(tempClassIds)

                selected_class_ids = classIds.joinToString(" , ")
                selected_class_names = classNames.joinToString(" , ")
                binding.selectedclass.text = selected_class_names
                Log.d("SelectedClass", selected_class_ids.toString())

                callStudents()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                classNames.clear()
                classIds.clear()
                selected_class_ids = ""
                selected_class_names = ""
                binding.selectedclass.text = ""

                studentNames.clear()
                studentIds.clear()
                selected_student_names = ""
                selected_student_ids = ""
                binding.selectedstudent.text = ""

                usersIds.clear()
                usersList.clear()
                selected_users_ids = ""
                selected_users_names = ""
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
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                studentNames.clear()
                studentIds.clear()
                studentNames.addAll(tempClassNames)
                studentIds.addAll(tempClassIds)

                selected_student_ids = studentIds.joinToString(" , ")
                selected_student_names = studentNames.joinToString(" , ")
                binding.selectedstudent.text = selected_student_names
                Log.d("selectedStudents", selected_student_ids.toString())

                callUsers()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                studentNames.clear()
                studentIds.clear()
                selected_student_names = ""
                selected_student_ids = ""
                binding.selectedstudent.text = ""

                usersIds.clear()
                usersList.clear()
                selected_users_ids = ""
                selected_users_names = ""
            }
            .create()
        dialog.show()
    }

    private fun showusers() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val listView = dialogView.findViewById<ListView>(R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(R.id.nameTv)

        nameTv.text = "Select User"

        val originalClassNames = mutableListOf("Select All") + usersList.map { it.employee_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = usersNames.toMutableSet()
        val tempClassIds = usersIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, filteredClassNames)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Restore previously selected checkboxes
        usersList.forEachIndexed { index, classItem ->
            if (tempClassIds.contains(classItem.id)) {
                checkedItems[index + 1] = true // Offset by 1 due to "Select All"
                listView.setItemChecked(index + 1, true) // Ensure check is shown
            }
        }

        // Check "Select All" if all are already selected
        if (tempClassIds.size == usersList.size) {
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
                    tempClassNames.addAll(usersList.map { it.employee_name })
                    tempClassIds.addAll(usersList.map { it.id })
                } else {
                    tempClassNames.clear()
                    tempClassIds.clear()
                }
            } else {
                val selectedClassName = usersList[which - 1].employee_name
                val selectedClassId = usersList[which - 1].id

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
                checkedItems[0] = tempClassNames.size == usersList.size
                listView.setItemChecked(0, checkedItems[0])
            }
        }

        // Implement search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredClassNames.clear()
                filteredClassNames.add("Select All") // Keep Select All on top
                if (newText.isNullOrEmpty()) {
                    filteredClassNames.addAll(usersList.map { it.employee_name })
                } else {
                    filteredClassNames.addAll(usersList.filter { it.employee_name.contains(newText, true) }.map { it.employee_name })
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
                usersNames.clear()
                usersIds.clear()
                usersNames.addAll(tempClassNames)
                usersIds.addAll(tempClassIds)

                selected_users_ids = usersIds.joinToString(" , ")
                selected_users_names = usersNames.joinToString(" , ")
                binding.selectedusers.text = selected_users_names
                Log.d("selectedUsers", selected_users_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                usersIds.clear()
                usersList.clear()
                selected_users_ids = ""
                selected_users_names = ""
            }
            .create()
        dialog.show()
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

                selected_group_ids = groupIds.joinToString(" , ")
                selected_group_names = groupNames.joinToString(" , ")
                binding.selectedgroups.text = selected_group_names
                Log.d("selectedgroups", selected_group_ids.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                groupIds.clear()
                groupList.clear()
                selected_group_ids = ""
                selected_group_names = ""
                binding.selectedgroups.text =""
                callGroups()
            }
            .create()
        dialog.show()
    }


    fun convertDateFormat(oldDateString: String): String {
        return try {
            // 1) Parse the old format ("dd-MM-yyyy")
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = inputFormat.parse(oldDateString)

            // 2) Reformat to the new format ("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            if (date != null) {
                outputFormat.format(date)  // e.g. "2000-02-21"
            } else {
                "" // or handle parse error
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "" // or handle parse error
        }
    }

}