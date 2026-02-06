package com.iprism.school.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iprism.school.R
import com.iprism.school.activities.SuccessActivity
import com.iprism.school.databinding.SingleDiaryBottomSheetBinding
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.repositories.DiaryRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DiaryViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class SingleDiaryIBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: SingleDiaryBottomSheetBinding
    private lateinit var diariesViewModel: DiaryViewModel
    private var backendDate: String? = null
    private var selectedImageUri: Uri? = null

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var studentId: String? = null
    private var classId: String? = null
    private var sectionId: String? = null
    private var diaryType = ""
    private lateinit var user: User
    private lateinit var userDetails : HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentId = it.getString(ARG_STUDENT_ID)
            classId = it.getString(ARG_CLASS_ID)
            sectionId = it.getString(ARG_SECTION_ID)
            backendDate = it.getString(ARG_BACKEND_DATE)
        }

        Log.d(
            "SingleDiaryBottomSheet",
            "studentId=$studentId, classId=$classId, sectionId=$sectionId, backendDate=$backendDate"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SingleDiaryBottomSheetBinding.inflate(inflater, container, false)
        user = User(requireContext())
        userDetails = user.getNewUserDetails()
        binding.diaryTypeRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.class_work_rb -> {
                    diaryType = "cw"
                }

                R.id.home_work_rb -> {
                    diaryType = "hw"
                }
            }
        }
        initViewModel()
        handleUploadLo()
        observeInsertDiaryResponse()
        handleSaveBtn()
        handleCross()
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.TransparentBottomSheetTheme
    }


    private fun handleCross() {
        binding.crossImg.setOnClickListener { view ->
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        dialog?.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog)
                    .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun handleUploadLo() {
        binding.uploadFileLo.setOnClickListener { view ->
            selectImage()
        }
    }

    companion object {
        private const val ARG_STUDENT_ID = "student_id"
        private const val ARG_CLASS_ID = "class_id"
        private const val ARG_SECTION_ID = "section_id"
        private const val ARG_BACKEND_DATE = "backend_date"

        fun newInstance(
            studentId: String,
            classId: String,
            sectionId: String,
            backendDate: String
        ): SingleDiaryIBottomSheetFragment {
            return SingleDiaryIBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_STUDENT_ID, studentId)
                    putString(ARG_CLASS_ID, classId)
                    putString(ARG_SECTION_ID, sectionId)
                    putString(ARG_BACKEND_DATE, backendDate)
                }
            }
        }
    }

    private fun initViewModel() {
        val diaryRepository = DiaryRepository(requireContext())
        val diaryFactory = ViewModelFactory { DiaryViewModel(diaryRepository) }
        diariesViewModel = ViewModelProvider(this, diaryFactory)[DiaryViewModel::class.java]
    }

    private fun selectImage() {
        val items = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        AlertDialog.Builder(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
            .setTitle("Add Photo!")
            .setItems(items) { dialog, which ->
                when (items[which]) {
                    "Take Photo" -> openCamera()
                    "Choose from Gallery" -> openGallery()
                    "Cancel" -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun setFileName(uri: Uri?) {
        uri ?: return
        var fileName = ""

        requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
            }
        }

        binding.fileNameTxt.text = fileName
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                selectedImageUri = getImageUriFromBitmap(bitmap)
                setFileName(selectedImageUri)
            }
        }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                setFileName(it)
            }
        }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            "IMG_${System.currentTimeMillis()}",
            null
        )

        return Uri.parse(path)
    }

    private fun observeInsertDiaryResponse() {
        diariesViewModel.insertDiaryResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.saveDiaryBtn.isEnabled = false
                    binding.crossImg.isEnabled = false
                    binding.uploadFileLo.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.saveDiaryBtn.isEnabled = true
                    var intent = Intent(requireContext(), SuccessActivity::class.java)
                    intent.putExtra("tag", "Diary Inserted")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.progress.hideProgress()
                    binding.saveDiaryBtn.isEnabled = true
                    binding.crossImg.isEnabled = true
                    binding.uploadFileLo.isEnabled = true

                }
            }
        }
    }

    private fun handleSaveBtn() {
        binding.saveDiaryBtn.setOnClickListener { view ->
          if (diaryType.isEmpty()) {
                ToastUtils.showErrorCustomToast(requireContext(), "Please Select Class Work or Home Work..!")
            }else if (getDetails().isEmpty() && selectedImageUri == null) {
                ToastUtils.showErrorCustomToast(requireContext(), "Please Enter Details or Select Image..")
            } else {
                var request = DiaryApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(), classId!!, backendDate!!, getDetails(), "",
                    convertUriToBase64Image(selectedImageUri), 1, sectionId!!, "single",
                    studentId!!, diaryType, userDetails[User.ID].toString(), "insert")
              Log.d("SingleDiaryRequest", request.toString())
                diariesViewModel.insertDiary(request)
            }
        }

    }

    private fun getDetails() : String{
        return binding.detailsTxt.text.toString().trim()
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        if (imageUri == null) return ""

        return try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
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