package com.iprism.school.activities.album

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iprism.school.base.BaseActivity

import com.iprism.school.databinding.ActivityCreateAlbumsBinding

import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.repositories.AlbumsRepository
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AlbumsViewModel
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.ViewModelFactory
import com.yalantis.ucrop.UCrop

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreateAlbumsActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateAlbumsBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var albumsViewModel: AlbumsViewModel
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private var profileUri: Uri? = null
    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val croppedUri = result.data?.let { UCrop.getOutput(it) }
                croppedUri?.let { setProfileImage(it) }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = result.data?.let { UCrop.getError(it) }
                cropError?.printStackTrace()
            }
        }

    private fun setProfileImage(uri: Uri) {
        profileUri = uri

        binding.attachmentLo.visibility =
            if (profileUri != null) View.VISIBLE else View.GONE

        Glide.with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            //  .circleCrop()
            .into(binding.attachmentImg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createLaunchSomeActivity()
        initViewModel()
        handleDateLo()
        observeClassesResponse()
        observeSectionsResponse()
        handleBack()
        handleAddBtn()
        handleCreateBtn()
        observeInsertAlbumCoverResponse()
        var requestClasses = ClassTeacherApiRequest("", userDetails[User.ID].toString(), userDetails[User.SCHOOL_ID].toString(), userDetails[User.ACADEMIC_YEAR_ID].toString(),"classes")
        attendanceViewModel.fetchClasses(requestClasses)
    }

    private fun observeInsertAlbumCoverResponse() {
        albumsViewModel.insertAlbumCoverResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.backIv.isEnabled = false
                    binding.addBtn.isEnabled = false
                    binding.createBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.backIv.isEnabled = true
                    binding.addBtn.isEnabled = true
                    binding.createBtn.isEnabled = true
                    ToastUtils.showSuccessCustomToast(this, "Cover Image Added Successfully..!")
                    var intent = Intent(this, AlbumDetailsActivity::class.java)
                    intent.putExtra("albumId", result.data.id)
                    intent.putExtra("albumName", result.data.title)
                    startActivity(intent)
                }

                is UiState.Error -> {

                    binding.progress.hideProgress()
                    binding.backIv.isEnabled = true
                    binding.addBtn.isEnabled = true
                    binding.createBtn.isEnabled = true
                }
            }
        }
    }

    private fun handleCreateBtn() {
        binding.createBtn.setOnClickListener { view ->
            if (getDate().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Select Date..!")
            } else if (classId.equals("-1", true)){
                ToastUtils.showErrorCustomToast(this, "Please Select Class..!")
            } else if (sectionId.equals("-1", true)){
                ToastUtils.showErrorCustomToast(this, "Please Select Section..!")
            }  else if (getTitleName().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Enter Title..!")
            }else if (profileUri == null){
                ToastUtils.showErrorCustomToast(this, "Please Select Album Cover Image..!")
            } else {
                var request = AlbumCoverImagesApiRequest(userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(), classId, DateTimeUtils.dateMonthYear,
                    getDescription(), convertUriToBase64Image(profileUri), 1,
                    sectionId, getTitleName(), userDetails[User.ID].toString(), "insert")
                albumsViewModel.insertAlbumCover(request)

            }
        }
    }

    private fun getDate() : String{
        return binding.dateTxt.text.toString().trim()
    }

    private fun getTitleName() : String{
        return binding.titleTxt.text.toString().trim()
    }

    private fun getDescription() : String{
        return binding.descriptionTxt.text.toString().trim()
    }

    private fun createLaunchSomeActivity() {
        launchSomeActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data
                val imageUri = result.data?.data
                imageUri?.let { startCrop(it) }
            }
        }
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Note")
                .setMessage("Please Select Landscape Images for Better Experience..!")
                .setCancelable(false)
                .setPositiveButton("Okay") { dialog, _ ->
                    dialog.dismiss()
                    selectImage()
                }
                .show()
        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener { view ->
            DateTimeUtils.getDate(binding.dateTxt, true)
        }
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(this)
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val albumRepository = AlbumsRepository(this)
        val albumFactory = ViewModelFactory { AlbumsViewModel(albumRepository) }
        albumsViewModel = ViewModelProvider(this, albumFactory)[AlbumsViewModel::class.java]
    }

    private fun observeClassesResponse() {
        attendanceViewModel.classesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.classes.isNotEmpty()) {
                        var updatedList = result.data.classes.toMutableList()
                        updatedList.add(0, Class("-1", "Select Class"))
                        setupClassesAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.classesSp.adapter = adapter
        binding.classesSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    classId = genderTypes[position].class_id.toString()
                    if (!classId.equals("-1", true)) {
                        var requestClasses = ClassTeacherApiRequest(
                            classId,
                            userDetails[User.ID].toString(),
                            userDetails[User.SCHOOL_ID].toString(),
                            userDetails[User.ACADEMIC_YEAR_ID].toString(),
                            "sections"
                        )
                        attendanceViewModel.fetchSections(requestClasses)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupSectionsAdapter(genderTypes: List<Section>) {
        var namesList = genderTypes.map { it.section_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sectionsSp.adapter = adapter
        binding.sectionsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sectionId = genderTypes[position].section_id.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun observeSectionsResponse() {
        attendanceViewModel.sectionsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.sections.isNotEmpty()) {
                        var updatedList = result.data.sections.toMutableList()
                        updatedList.add(0, Section("-1", "Select Section"))
                        setupSectionsAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun selectImage() {
        var options = arrayOf<CharSequence>()
        options = arrayOf<CharSequence>("Choose From Gallery", "Camera", "Cancel")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Choose From Gallery") {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickPhoto.type = "image/*"
                launchSomeActivity!!.launch(pickPhoto)
            } else if (options[item] == "Camera") {
                if (checkPermissions()) {
                    launchCameraIntent()
                } else {
                    requestPermissions()
                }
            } else {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        val uCrop = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(512, 512)
            .withOptions(getUCropOptions())
        cropImageLauncher.launch(uCrop.getIntent(this))
    }

    private fun getUCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCircleDimmedLayer(false)
        options.setShowCropGrid(true)
        options.setShowCropFrame(true)
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(90)
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(false)

        return options
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            val bitmap = data.extras!!["data"] as Bitmap?
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            //  Base64.Encoder encoder = Base64.getEncoder();
            val uri: Uri = getImageUri(this@CreateAlbumsActivity, bitmap)!!
            uri?.let { startCrop(it) }
            /*  profileUri = uri
              binding.profileImg.visibility = View.VISIBLE
              binding.profileImg.setImageURI(profileUri)*/
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val file = File(inContext.cacheDir, "image.jpg")
        try {
            val out = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCameraIntent()
                } else {
                    showToast("Permission Denied")
                }
                return
            }

            else -> {

            }
        }
    }

    private fun launchCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        if (imageUri == null) return ""

        return try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                ) // Use PNG if you prefer lossless
                val imageBytes = byteArrayOutputStream.toByteArray()
                Base64.encodeToString(imageBytes, Base64.DEFAULT)
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

}