package com.iprism.school.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.DayCareStudentsAdapter
import com.iprism.school.adapters.ImageAdapter
import com.iprism.school.databinding.ActivityDaycareReportBinding
import com.iprism.school.model.Request.DaycarereportReq
import com.iprism.school.model.Request.ReportCreateReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherGroupStudentsReq
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.GroupsTeacher
import com.iprism.school.model.Response.TeacherGroupStudentsResponse
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DaycareReportActivity : BaseActivity() {

    private lateinit var binding: ActivityDaycareReportBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var id: String = ""
    private var name: String = ""
    private var type: String = ""
    private var group_id: String = ""

    private val groupNames = mutableListOf<String>()
    private val groupIds = mutableListOf<String>()
    private val groupList = mutableListOf<GroupsTeacher>()

    private var selected_group_ids : String? = ""
    private var selected_group_names : String? = ""
    private var imageCount : String? = ""

    private var notifySelect : String? = "no"
    private var editSelect : String? = "no"

    private var startTime : String? = ""

    private lateinit var dairiesAdapter: DayCareStudentsAdapter
    private var selectedGroupIds = "" // To store selected student IDs

    lateinit var resultLauncher: ActivityResultLauncher<Uri>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    private var commaSeparatedBase64 : String? = null
    private lateinit var imageAdapter: ImageAdapter
    private val imageUris = mutableListOf<Uri>()
    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaycareReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("name").toString()
        type = intent.getStringExtra("type").toString()
        group_id = intent.getStringExtra("group_id").toString()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        binding.imagesRv.layoutManager = GridLayoutManager(this,3)
        imageAdapter = ImageAdapter(imageUris) { uri ->
        imageAdapter.deleteImage(uri)
        }

        binding.imagesRv.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()

        Log.d("images_Uris",imageUris.toString())

        binding.dayCareNameTxt.text = name.toString()

        if (type == "activity"){
            binding.mealNameLL.visibility = View.GONE
            binding.startEndTimingsLl.visibility = View.VISIBLE
            startTime = binding.startTimeTxt.text.toString()
        }else{
            startTime = binding.timeTv.text.toString()
            binding.mealNameLL.visibility = View.VISIBLE
            binding.startEndTimingsLl.visibility = View.GONE
        }


        handleBack()
        handleDateLo()
        handleStartDateLo()
        handleEndTimeLo()
        handleSendBtn()

        callGroups()

        binding.timgll.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTv)
        })

        resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d("cameraCapture", "Photo captured at: $photoUri")
                imageUris.add(photoUri)
                imageAdapter.notifyDataSetChanged()
            } else {
                Log.e("cameraCapture", "Failed to capture photo")
            }
        }

        binding.cameraIv.setOnClickListener(View.OnClickListener {
//            showOptionsDialog()
            selectImage()
        })

        binding.groupLl.setOnClickListener {
            showGroups()
        }


        // Select All checkbox listener
        binding.checkBoxAll.setOnCheckedChangeListener { _, isChecked ->
            dairiesAdapter.updateSelectAllState(isChecked)  // Update select all state in the adapter
            selectedGroupIds = if (isChecked) {
                dairiesAdapter.getAllIds().joinToString(",")  // Get all group IDs if select all is checked
            } else {
                ""
            }
            Log.d("selected_IDS", selectedGroupIds)  // Log the selected IDs
        }

        binding.checkNotify.setOnCheckedChangeListener { _, isChecked ->
            notifySelect = if (isChecked) "yes" else "no"
            Log.d("NotifyValue", "Notify is: $notifySelect") // For debugging
        }

        binding.checkBoxEdit.setOnCheckedChangeListener { _, isChecked ->
            editSelect = if (isChecked) "yes" else "no"
            Log.d("EditValue", "Edit is: $editSelect") // For debugging
        }
    }

    private fun handleSendBtn() {
        binding.sendBtn.setOnClickListener(View.OnClickListener {
            convertImagesToBase64()
            if (selected_group_ids == ""||selected_group_ids == null){
                ToastUtils.showErrorCustomToast(this@DaycareReportActivity, "Select Group")
            }else if (selectedGroupIds == ""||selectedGroupIds == null){
                ToastUtils.showErrorCustomToast(this@DaycareReportActivity, "Select Students")
            } else if (binding.remarksEt.text.toString()== ""||binding.remarksEt.text.toString()== null){
                ToastUtils.showErrorCustomToast(this@DaycareReportActivity, "Enter Remarks")
            }else{

                if (type == "activity"){
                    binding.mealNameLL.visibility = View.GONE
                    binding.startEndTimingsLl.visibility = View.VISIBLE
                    startTime = binding.startTimeTxt.text.toString()
                }else{
                    startTime = binding.timeTv.text.toString()
                    binding.mealNameLL.visibility = View.VISIBLE
                    binding.startEndTimingsLl.visibility = View.GONE
                }

                callsendReport()
            }

        })
    }

    private fun handleEndTimeLo() {
        binding.endTimeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.endTimeTxt)
        })
    }

    private fun handleStartDateLo() {
        binding.startTimeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.startTimeTxt)
        })
    }

    private fun handleDateLo() {
            val calendar: Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val stf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val formattedDate: String = sdfString.format(calendar.time)
            val formattedTime = stf.format(calendar.time)
            binding.dateTxt.text = formattedDate
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
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
                        ToastUtils.showSuccessCustomToast(this@DaycareReportActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(this@DaycareReportActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DaycareReportActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DaycareReportActivity, t.message.toString())
            }
        })
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

                selected_group_ids = groupIds.joinToString(",")
                selected_group_names = groupNames.joinToString(" , ")
                binding.selectedgroup.text = selected_group_names
                Log.d("selectedgroups", selected_group_ids.toString())

                callStudentsnew()

            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                groupIds.clear()
                groupList.clear()
                selected_group_ids = ""
                selected_group_names = ""
                binding.selectedgroup.text =""
            }
            .create()
        dialog.show()
    }

    private fun callStudentsnew() {
        showProgress()
        var loginApiRequest = TeacherGroupStudentsReq( auth_token,binding.dateTxt.text.toString(),selected_group_ids.toString(),scl_id,teacherId,"")
        Log.d("group_students_Req", loginApiRequest.toString())
        val call: Call<TeacherGroupStudentsResponse> = parentApiService!!.teacherGroupStudents(loginApiRequest)
        call.enqueue(object : Callback<TeacherGroupStudentsResponse> {
            override fun onResponse(
                call: Call<TeacherGroupStudentsResponse>,
                response: Response<TeacherGroupStudentsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()

                    val loginApiResponse = response.body()
                    Log.d("loginApiResponse", loginApiResponse.toString())

                    if (loginApiResponse != null && loginApiResponse.status) {
                        if (loginApiResponse.response.groups.isEmpty()) {
                            binding.nodataTv.visibility = View.VISIBLE
                            binding.rvList.visibility = View.GONE
                        } else {
                            binding.nodataTv.visibility = View.GONE
                            binding.rvList.visibility = View.VISIBLE

                            dairiesAdapter = DayCareStudentsAdapter(
                                loginApiResponse.response.groups, { selectedIdsList ->
                                    selectedGroupIds = selectedIdsList.joinToString(",")
                                    Log.d("selected_IDs", selectedGroupIds)
                                },
                                selectAll = false
                            )
                            binding.rvList.adapter = dairiesAdapter
                            binding.rvList.layoutManager = GridLayoutManager(this@DaycareReportActivity, 3)
                        }
                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@DaycareReportActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DaycareReportActivity, response.message())
                }
            }

            override fun onFailure(call: Call<TeacherGroupStudentsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DaycareReportActivity, t.message.toString())
            }
        })
    }


    private fun callsendReport() {
        showProgress()
        var loginApiRequest = DaycarereportReq( commaSeparatedBase64.toString(),auth_token,
            binding.dateTxt.text.toString(),binding.endTimeTxt.text.toString(), group_id,
            binding.mealNameEt.text.toString(),notifySelect.toString(),binding.qntyEt.text.toString()
            ,binding.remarksEt.text.toString(),
            type,scl_id,startTime.toString(),selectedGroupIds,teacherId,editSelect.toString())

        Log.d("reports_Req", loginApiRequest.toString())
        val call: Call<ReportCreateReq> = parentApiService!!.daycareReport(loginApiRequest)
        call.enqueue(object : Callback<ReportCreateReq> {
            override fun onResponse(
                call: Call<ReportCreateReq>, response: Response<ReportCreateReq>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    Log.d("loginApiResponse", loginApiResponse.toString())
                    ToastUtils.showSuccessCustomToast(this@DaycareReportActivity,loginApiResponse!!.message.toString())
                    var intent = Intent(this@DaycareReportActivity, HomeActivity::class.java)
                    intent.putExtra("tag", "DayCare")
                    startActivity(intent)
                    finish()

                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DaycareReportActivity, "Failed")
                }
            }

            override fun onFailure(call: Call<ReportCreateReq>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DaycareReportActivity, "Response Failed")
            }
        })
    }

    private fun encodeUriToBase64(uri: Uri): String {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this@DaycareReportActivity,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this@DaycareReportActivity)
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
                    photoUri = createImageUri()
                    resultLauncher.launch(photoUri)
//                    resultLauncher.launch(intent)
//                    pickImageLauncher.launch(intent)
                }
            }
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            this?.let {
                intent.resolveActivity(this.packageManager)?.also {
                    pickImageLauncher.launch(intent)
                }
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data?.data != null) {
            val imageUri = result.data!!.data!!
            imageUris.add(imageUri)
            imageAdapter.notifyDataSetChanged()
            convertImagesToBase64()
        }
    }

    private fun convertImagesToBase64() {
        val base64Strings = imageUris.mapNotNull { uri -> uriToBase64(this@DaycareReportActivity, uri) }
        commaSeparatedBase64 = base64Strings.joinToString(",")
        Log.d("base64String", commaSeparatedBase64.toString())

        imageCount = imageUris.size.toString()

        binding.imageCount.text = imageCount.toString()
    }

    private fun uriToBase64(context: DaycareReportActivity, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Create a URI where the camera image should be saved.
    private fun createImageUri(): Uri {
        // Create a unique file name in the app's external pictures directory.
        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "camera_image_${System.currentTimeMillis()}.jpg")
        // Get URI using FileProvider.
        return FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
    }

}