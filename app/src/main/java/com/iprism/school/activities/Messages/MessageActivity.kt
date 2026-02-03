package com.iprism.school.activities.Messages

import android.app.DatePickerDialog
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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.ImageAdapter
import com.iprism.school.databinding.ActivityMessageBinding
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.CreateNewMsgReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.AttendanceUpdatedResponse
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.GroupsTeacher
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.StaffDetailList
import com.iprism.school.model.Response.StudentList
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
import java.util.Calendar

class MessageActivity : BaseActivity() {

    private lateinit var binding: ActivityMessageBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var emp_designation: String = ""
    private var emp_name: String = ""

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

    private var message_type : String? = "normal"

    private var disablereplay : String? = "no"

    private var shecduled_value  = 0

    lateinit var resultLauncher: ActivityResultLauncher<Uri>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    private var commaSeparatedBase64 : String? = null
    private lateinit var imageAdapter: ImageAdapter
    private val imageUris = mutableListOf<Uri>()
    private lateinit var photoUri: Uri

    private var attachment_type : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleSendMessageBtn()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        emp_name = userDetails[User.EMP_NAME].toString()
        emp_designation = userDetails[User.EMP_DESIGNATION].toString()

        binding.empNameTv.text =" Name : "+userDetails[User.EMP_NAME].toString()
        binding.empDesignationTv.text = "Designation : "+userDetails[User.EMP_DESIGNATION].toString()

        binding.imagesRv.layoutManager = GridLayoutManager(this,3)
        imageAdapter = ImageAdapter(imageUris) { uri ->
            imageAdapter.deleteImage(uri) }
        binding.imagesRv.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()


        binding.imagesRv

        binding.cameraImg.setOnClickListener(View.OnClickListener {
//            showOptionsDialog()
            selectImage()
        })

        binding.backImg.setOnClickListener {
            val intent = Intent(this@MessageActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.groupsLl.setOnClickListener {

        }

        binding.classLl.setOnClickListener {
        }

        binding.checkBoxSCH.setOnCheckedChangeListener { _, isChecked ->
            binding.dateTimeLl.visibility = if (isChecked) LinearLayout.VISIBLE else LinearLayout.GONE
            shecduled_value = if (isChecked) 1 else 0
            message_type = if (isChecked) "scheduled" else "normal"
        }

        binding.checkBoxDR.setOnCheckedChangeListener { _, isChecked ->
            disablereplay = if (isChecked) "yes" else "no"
        }

        binding.studentLl.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Class")
            }else{
            }
        }

        binding.usersLl.setOnClickListener {
            if (selected_class_ids == ""||selected_class_ids == null){
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Class")
            }else if (selected_student_ids == ""||selected_student_ids == null){
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Students")
            } else{
                showusers()
            }
        }

        binding.groupsLl.setOnClickListener {
        }

        binding.timeLl.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })

        binding.dateLl.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(binding.root.context, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.dateTxt.text = formattedDate
            }, year, month, day)

            // Set minimum date to today (only allow future dates)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        })
    }

    private fun handleSendMessageBtn() {
        binding.sendMessageBtn.setOnClickListener(View.OnClickListener {

            convertImagesToBase64()
            if (selected_class_ids == "" || selected_class_ids == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Class")
            } else if (selected_student_ids == "" || selected_student_ids == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Students")
            } else if (selected_users_ids == "" || selected_users_ids == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Users")
            } else if (selected_group_ids == "" || selected_group_ids == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Group")
            } else if (binding.textView10.text.toString() == "" || binding.textView10.text.toString() == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Enter Subject")
            } else if (binding.dateTxt.text.toString() == "" || binding.dateTxt.text.toString() == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Date")
            } else if (binding.timeTxt.text.toString() == "" || binding.timeTxt.text.toString() == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Select Time")
            } else if (binding.textView10.text.toString() == "" || binding.textView10.text.toString() == null) {
                ToastUtils.showSuccessCustomToast(this@MessageActivity, "Enter Details")
            } else {
                showConfirmationDialog()
            }
        })
    }

    private fun showConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.message_coonfirmation_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
       val yesBtn = dialogView.findViewById(R.id.yes_btn) as Button
       val noBtn = dialogView.findViewById(R.id.no_btn) as Button
        noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        yesBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
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

                selected_users_ids = usersIds.joinToString(",")
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

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this@MessageActivity,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this@MessageActivity)
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

        }
    }

    private fun convertImagesToBase64() {

        attachment_type = "image"
        val base64Strings = imageUris.mapNotNull { uri -> uriToBase64(this@MessageActivity, uri) }
        commaSeparatedBase64 = base64Strings.joinToString(",")
        Log.d("base64String", commaSeparatedBase64.toString())
    }

    private fun uriToBase64(context: MessageActivity, uri: Uri): String? {
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@MessageActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()

    }

}