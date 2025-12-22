package com.iprism.school.activities.album

import android.Manifest
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.ImageAdapter
import com.iprism.school.adapters.VideoAdapter
import com.iprism.school.databinding.ActivityCreateAlbumsBinding
import com.iprism.school.model.Request.CreateAlbumReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.AlbumUploadResponse
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.GroupsTeacher
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CreateAlbumsActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateAlbumsBinding

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var emp_designation: String = ""
    private var emp_name: String = ""

    private val classNames = mutableListOf<String>()
    private val classIds = mutableListOf<String>()
    private val classList = mutableListOf<ClasseList>()

    private val groupNames = mutableListOf<String>()
    private val groupIds = mutableListOf<String>()
    private val groupList = mutableListOf<GroupsTeacher>()

    private var selected_class_ids: String? = ""
    private var selected_class_names: String? = ""

    private var selected_group_ids: String? = ""
    private var selected_group_names: String? = ""

    private var isVisible = false  // Initially hidden

    private var commaSeparatedBase64: String? = ""
    private var base64StringVideo: String? = ""

    private lateinit var imageAdapter: ImageAdapter
    private val imageUris = mutableListOf<Uri>()
    private lateinit var photoUri: Uri

    private var attachment_type: String? = ""
    private var visiable_type: String? = ""

    private lateinit var videoAdapter: VideoAdapter
    private val videoUris = mutableListOf<Uri>()
    private val REQUEST_VIDEO_PICK = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_VIDEO), 100)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        emp_name = userDetails[User.Companion.EMP_NAME].toString()
        emp_designation = userDetails[User.Companion.EMP_DESIGNATION].toString()

        handleBack()
        handleCreateBtn()
        handleAddBtn()

        // Private - Visible to only School parent
        // Public - Visible to Everyone

        val genderoptions = arrayOf("Private - Visible to only School parent", "Public - Visible to Everyone")
        binding.privatePublicLl.setOnClickListener {
            // Track the selected option
            var selectedOption = ""
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an Option")
            builder.setSingleChoiceItems(genderoptions, -1) { dialog, which ->
                selectedOption = genderoptions[which] // Capture the selected option
            }
            builder.setPositiveButton("OK") { dialog, _ ->
                if (selectedOption.isNotEmpty()) {
                    visiable_type = selectedOption.toString()
                    binding.genderText.text = selectedOption.toString()

                    visiable_type = if (selectedOption == "Private - Visible to only School parent") "Private" else "Public"

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

        binding.imagesRv.layoutManager = GridLayoutManager(this, 3)
        imageAdapter = ImageAdapter(imageUris) { uri ->
            imageAdapter.deleteImage(uri)
        }
        binding.imagesRv.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()

        binding.videosRv.layoutManager = GridLayoutManager(this, 3)
        videoAdapter = VideoAdapter(videoUris) { uri ->
            deleteVideo(uri)
        }
        binding.videosRv.adapter = videoAdapter

        callclasses()

        binding.classLl.setOnClickListener {
            showClasses()
        }
        binding.groupsLl.setOnClickListener {
            showGroups()
        }

        binding.imagesImg.setOnClickListener {
            binding.imagesRv.visibility = View.VISIBLE
            binding.videosRv.visibility = View.GONE
            openGallery()
            attachment_type = "image"
        }

        binding.videosImg.setOnClickListener {
            binding.imagesRv.visibility = View.GONE
            binding.videosRv.visibility = View.VISIBLE
            selectVideos()
            attachment_type = "video"
        }

    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            isVisible = !isVisible  // Toggle state
            binding.optionsLo.visibility = if (isVisible) View.VISIBLE else View.GONE
//            binding.optionsLo.visibility = View.VISIBLE
        })
    }

    private fun handleCreateBtn() {
        binding.createBtn.setOnClickListener(View.OnClickListener {
            blinkButton(binding.createBtn)
//            showConfirmationBottomSheet()

            if (attachment_type == "image") {
                convertImagesToBase64()
            } else {

            }

            if (binding.titleEt.text.toString() == "" || binding.titleEt.text.toString() == null) {
                showToast("Enter Title")
            } else if (binding.descriptionEt.text.toString() == "" || binding.descriptionEt.text.toString() == null) {
                showToast("Enter Description")
            } else if (selected_class_ids == "" || selected_class_ids == null) {
                showToast("Select Class")
            }else if (commaSeparatedBase64 == "" || commaSeparatedBase64 == null) {
                showToast("Select at least one Album")
            } else {
                uploadAlbum()
            }

        })
    }

    private fun blinkButton(button: Button) {
        val blinkAnimation = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f)
        blinkAnimation.duration = 500 // 500ms for the blink
        blinkAnimation.repeatCount = 0 // Blink twice
        blinkAnimation.start()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@CreateAlbumsActivity, AlbumsActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun showConfirmationBottomSheet() {

//        val bottomSheetDialog = BottomSheetDialog(this)
//        val binding = AlbumConfirmationBottomSheetBinding.inflate(layoutInflater)
//        bottomSheetDialog.setContentView(binding.root)
//        bottomSheetDialog.setOnShowListener { dialog ->
//            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)
//            bottomSheet?.setBackgroundResource(com.iprism.school.R.drawable.rounded_bottom_sheet_background)
//            binding.confirmBtn.setOnClickListener(View.OnClickListener {
//                bottomSheetDialog.dismiss()
//                ToastUtils.showSuccessCustomToast(this, "Album Created Successfully")
//                finish()
//            })
//
//            binding.crossIv.setOnClickListener(View.OnClickListener {
//                bottomSheetDialog.dismiss()
//            })
//
//            binding.noBtn.setOnClickListener(View.OnClickListener {
//                bottomSheetDialog.dismiss()
//            })
//        }
//        bottomSheetDialog.show()

    }

    private fun callclasses() {
        showProgress()
        var loginApiRequest = TeacherAccessReq(teacherId, auth_token)
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
                    }

                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(
                            this@CreateAlbumsActivity,
                            loginApiResponse.message.toString()
                        )
                        if (loginApiResponse.message.toString() == "Authentication Token Expired") {
                            user!!.storeUserDetails(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                            startActivity(
                                Intent(
                                    this@CreateAlbumsActivity,
                                    LoginActivity::class.java
                                )
                            )
                            finish()
                        } else {

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateAlbumsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateAlbumsActivity, t.message.toString())
            }
        })
    }

    private fun callGroups() {
        showProgress()
        var loginApiRequest = SchoolStaffReq(auth_token, scl_id, teacherId)
        Log.d("class_Req_2025", loginApiRequest.toString())
        var call: Call<GroupsResponse> = parentApiService!!.teacherViewGroups(loginApiRequest)
        call.enqueue(object : Callback<GroupsResponse> {
            override fun onResponse(
                call: Call<GroupsResponse>,
                response: Response<GroupsResponse>
            ) {
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
                        ToastUtils.showSuccessCustomToast(
                            this@CreateAlbumsActivity,
                            loginApiResponse.message.toString()
                        )
                        if (loginApiResponse.message.toString() == "Authentication Token Expired") {
                            user!!.storeUserDetails(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                            startActivity(
                                Intent(
                                    this@CreateAlbumsActivity,
                                    LoginActivity::class.java
                                )
                            )
                            finish()
                        } else {

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateAlbumsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateAlbumsActivity, t.message.toString())
            }
        })
    }

    private fun showClasses() {

        val dialogView = LayoutInflater.from(this).inflate(com.iprism.school.R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(com.iprism.school.R.id.searchView)
        val listView = dialogView.findViewById<ListView>(com.iprism.school.R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(com.iprism.school.R.id.nameTv)

        nameTv.text = "Select Class"

        val originalClassNames = mutableListOf("Select All") + classList.map { it.class_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = classNames.toMutableSet()
        val tempClassIds = classIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            filteredClassNames
        )
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
                    filteredClassNames.addAll(classList.filter {
                        it.class_name.contains(
                            newText,
                            true
                        )
                    }.map { it.class_name })
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
                binding.selectedclass.text = selected_class_names
                Log.d("SelectedClass", selected_class_ids.toString())
                callGroups()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Reset all selections
                classNames.clear()
                classIds.clear()
                selected_class_ids = ""
                selected_class_names = ""
                binding.selectedclass.text = ""

            }
            .create()
        dialog.show()
    }

    private fun showGroups() {
        val dialogView = LayoutInflater.from(this).inflate(com.iprism.school.R.layout.dialog_class_selection, null)
        val searchView = dialogView.findViewById<SearchView>(com.iprism.school.R.id.searchView)
        val listView = dialogView.findViewById<ListView>(com.iprism.school.R.id.classListView)
        val nameTv = dialogView.findViewById<TextView>(com.iprism.school.R.id.nameTv)

        nameTv.text = "Select Groups"

        val originalClassNames = mutableListOf("Select All") + groupList.map { it.group_name }
        val filteredClassNames = originalClassNames.toMutableList()
        val checkedItems = BooleanArray(originalClassNames.size) { false }

        // Track selected class IDs
        val tempClassNames = groupNames.toMutableSet()
        val tempClassIds = groupIds.toMutableSet()

        // Set up the adapter
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            filteredClassNames
        )
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
                    filteredClassNames.addAll(groupList.filter {
                        it.group_name.contains(
                            newText,
                            true
                        )
                    }.map { it.group_name })
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
                groupNames.clear()
                groupIds.clear()
                groupNames.addAll(tempClassNames)
                groupIds.addAll(tempClassIds)

                selected_group_ids = groupIds.joinToString(",")
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
                binding.selectedgroups.text = ""
            }
            .create()
        dialog.show()
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
        val base64Strings =
            imageUris.mapNotNull { uri -> uriToBase64(this@CreateAlbumsActivity, uri) }
        commaSeparatedBase64 = base64Strings.joinToString(",")
        Log.d("base64String", commaSeparatedBase64.toString())
    }

    private fun uriToBase64(context: CreateAlbumsActivity, uri: Uri): String? {
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

    private fun selectVideos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "video/*" // Correct way to set MIME type
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)  // Allow multiple selection
        }
        startActivityForResult(intent, REQUEST_VIDEO_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_VIDEO_PICK && resultCode == RESULT_OK) {
            videoUris.clear()

            // If multiple videos are selected
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    videoUris.add(clipData.getItemAt(i).uri)
                }
            }
            data?.data?.let { uri ->
                videoUris.add(uri)
            }


            // Convert videos to Base64
            val base64Videos = videoUris.mapNotNull { uri -> uriToBase64(uri) }

            // Create a comma-separated Base64 string
            commaSeparatedBase64 = base64Videos.joinToString(",")

            Log.d("base64StringVideo", commaSeparatedBase64.toString())

            // Refresh RecyclerView
//            videoAdapter.updateVideos(videoUris)
            videoAdapter.notifyDataSetChanged()

//            Toast.makeText(this, "Selected ${videoUris.size} videos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
            inputStream?.close()
            byteArrayOutputStream.close()

            // Convert to Base64 string
            Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun deleteVideo(uri: Uri) {
        videoAdapter.deleteVideo(uri)
        Toast.makeText(this, "Video deleted", Toast.LENGTH_SHORT).show()
        // Convert videos to Base64
        val base64Videos = videoUris.mapNotNull { uri -> uriToBase64(uri) }

        // Create a comma-separated Base64 string
        commaSeparatedBase64 = base64Videos.joinToString(",")

        Log.d("base64StringVideo", commaSeparatedBase64.toString())

    }

    private fun uploadAlbum() {
        showProgress()
        var apiRequest = CreateAlbumReq(
            attachment_type.toString(),
            commaSeparatedBase64.toString(),
            auth_token,
            selected_class_ids.toString(),
            binding.descriptionEt.text.toString(),
            selected_group_ids.toString(),
            scl_id,
            teacherId,
            binding.titleEt.text.toString(),
            visiable_type.toString(), "")
        Log.d("upload__Album_Req", apiRequest.toString())
        val call: Call<AlbumUploadResponse> = parentApiService!!.teacherUploadAlbum(apiRequest)
        call.enqueue(object : Callback<AlbumUploadResponse> {
            override fun onResponse(call: Call<AlbumUploadResponse>, response: Response<AlbumUploadResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    ToastUtils.showSuccessCustomToast(this@CreateAlbumsActivity, loginApiResponse!!.message.toString())
                    val intent = Intent(this@CreateAlbumsActivity, CreateAlbumsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateAlbumsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<AlbumUploadResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateAlbumsActivity, t.message.toString())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@CreateAlbumsActivity, AlbumsActivity::class.java)
        startActivity(intent)
        finish()
    }


}