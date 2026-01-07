package com.iprism.school.fragments

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
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
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseFragment
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.DairiesAdapter
import com.iprism.school.adapters.DairiesNewAdapter
import com.iprism.school.databinding.FragmentDiaryBinding
import com.iprism.school.databinding.StudentRemarksBinding
import com.iprism.school.model.Request.DairyStudentUpdateReq
import com.iprism.school.model.Request.DairyStudentsReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
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
import java.time.LocalDate
import java.util.ArrayList
import java.util.Base64
import java.util.Calendar
import java.util.Locale

class DiaryFragment : BaseFragment() {

    private lateinit var binding : FragmentDiaryBinding
    private lateinit var binding1 : StudentRemarksBinding

    private val viewModel: Scl_ViewModel by viewModels()
    private lateinit var adapter: DairiesNewAdapter

    private var classId : String? = ""
    private val class_names: ArrayList<String> = ArrayList<String>()
    private val class_ids: ArrayList<String> = ArrayList<String>()

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var checkType: String = "class_work"
    private var studentId: String = ""
    private var oldRemarks: String = ""

    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""

    private var currentDate: String? = ""
    private var picType: String? = ""
    private var studentsCount: String? = ""
    private val CAMERA_PERMISSION_CODE = 100

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()

        currentDate = LocalDate.now().toString()
        binding.etDob.text = currentDate.toString()
        Log.d("today_Date",currentDate.toString())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted, therefore request it
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            // Permission has already been granted
            // Proceed with your logic here
        }

        binding.allPicImg.setOnClickListener {
            binding.detailsLl.visibility = View.VISIBLE
            if (studentsCount == ""||studentsCount == null||studentsCount.toString() <= "0"){
                ToastUtils.showSuccessCustomToast(requireContext(), "Select Students")
            }else if (binding.etDetails.text.toString() == null||binding.etDetails.text.toString() == ""){
                ToastUtils.showSuccessCustomToast(requireContext(), "Enter Details")
            }else if (binding.checkBoxall.isChecked  == true){
                selectImage()
            }else{
                ToastUtils.showSuccessCustomToast(requireContext(), "Select All Check box")
            }
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
                encodedPic = encoder.encodeToString(b)

                if (picType == "all"){
                    val updateReq = DairyStudentUpdateReq(encodedPic.toString(),"image",auth_token,
                        classId.toString(),currentDate.toString(),binding.etDetails.text.toString(),"",
                        "all",teacherId,checkType)
                    callUpdatePic(updateReq)
                }else{

                    val updateReq = DairyStudentUpdateReq(encodedPic.toString(),"image",auth_token,
                        classId.toString(),currentDate.toString(),binding.etDetails.text.toString(),oldRemarks,
                        studentId,teacherId,checkType)
                        callUpdatePic(updateReq)
                }
            }
        }

        resultLaunchergallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val uri = data?.data
                val imagestrem : InputStream? = activity?.contentResolver?.openInputStream(uri!!)
                val selectedImage  : Bitmap = BitmapFactory.decodeStream(imagestrem)
                encodedPic = encodeImage(selectedImage)

                if (picType == "all"){
                    val updateReq = DairyStudentUpdateReq(encodedPic.toString(),"image",auth_token,
                        classId.toString(),currentDate.toString(),binding.etDetails.text.toString(),"",
                        "all",teacherId,checkType)
                    callUpdatePic(updateReq)
                }else{
                    val updateReq = DairyStudentUpdateReq(encodedPic.toString(),"image",auth_token,
                        classId.toString(),currentDate.toString(),binding.etDetails.text.toString(),oldRemarks,
                        studentId,teacherId,checkType)
                    callUpdatePic(updateReq)
                }
            }
        }

        callclasses()

        binding.dateLl.setOnClickListener {
            showDatePickerDialog()
        }

        binding.checkBox1.isChecked = true

        binding.spClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.BLACK)
                (parent!!.getChildAt(0) as TextView).textSize = 14f
                classId = class_ids[position]
//                callStudents()
                var loginApiRequest = DairyStudentsReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
                callMVVM(loginApiRequest)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.checkBoxall.setOnCheckedChangeListener { _, isChecked ->
            if (binding.checkBoxall.isChecked){
                binding.detailsLl.visibility = View.VISIBLE
            }else{
                binding.detailsLl.visibility = View.GONE
            }
        }

        // Ensure only one checkbox is selected at a time
        binding.checkBox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.checkBox2.isChecked = false
            if (binding.checkBox1.isChecked){
                checkType = "class_work"
                var loginApiRequest = DairyStudentsReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
                callMVVM(loginApiRequest)
            }
        }

        binding.checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.checkBox1.isChecked = false
            if (binding.checkBox2.isChecked){
                checkType = "home_work"
                var loginApiRequest = DairyStudentsReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
                callMVVM(loginApiRequest)
            }
        }

        binding.classLl.setOnClickListener {
            binding.spClass.performClick()
            callclasses()
        }

        return binding.root
    }

    private fun callUpdatePic(updateReq: DairyStudentUpdateReq) {
        showProgress()
        Log.d("updateReq",updateReq.toString())
        viewModel.dairyStudentUpdate(updateReq).observe(requireActivity(), Observer { response ->
            if (response != null && response.status == true ) {
                hideProgress()
                studentId = ""
                Log.d("updatePic",response.toString())
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                binding.etDetails.text.clear()
                var loginApiRequest = DairyStudentsReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
                callMVVM(loginApiRequest)
            } else {
                hideProgress()
                Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun callStudents() {
        showProgress()
        var loginApiRequest = DairyStudentsReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
//        var loginApiRequest = StudentsNewReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
        Log.d("class_Students_Req", loginApiRequest.toString())
        var call: Call<Class_studentResponse> = parentApiService!!.class_studentsnew(loginApiRequest)
        call.enqueue(object : Callback<Class_studentResponse> {
            override fun onResponse(call: Call<Class_studentResponse>, response: Response<Class_studentResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    Log.d("class_Students_Response", loginApiResponse.toString())
                    if (loginApiResponse!!.status) {
                        hideProgress()
                        if (loginApiResponse.response.students.isEmpty()){
                            binding.nodataTv.visibility = View.VISIBLE
                            binding.dairyRv.visibility = View.GONE
                        }else{
                            binding.nodataTv.visibility = View.GONE
                            binding.dairyRv.visibility = View.VISIBLE
                            var dairiesAdapter = DairiesAdapter(requireContext(),loginApiResponse.response.students)
                            binding.dairyRv.adapter = dairiesAdapter
                            var layoutManager = LinearLayoutManager(requireContext())
                            binding.dairyRv.layoutManager = layoutManager

                            dairiesAdapter.OnItemBtn = {
                                    mydata ->
                                val et_details = binding.etDetails.text.toString()
                                if (checkType == ""|| checkType == null){
                                    ToastUtils.showSuccessCustomToast(requireContext(), "Select Work Type")
                                }else if (binding.etDetails.text.toString() == ""||binding.etDetails.text.toString() == null){
                                    ToastUtils.showSuccessCustomToast(requireContext(), "Enter Details")
                                }else {
                                    val studentId = mydata.id.toString()
//                                    val oldremark = mydata.remarks.toString()
                                     oldRemarks = mydata.remarks.toString()
                                    callBottomSheet1(studentId,oldRemarks)
                                }
                            }
                        }

                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.dairyRv.visibility = View.GONE

//                        ToastUtils.showSuccessCustomToast(requireContext(), loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            activity!!.finish()
                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(requireContext(), "Failed")
                }
            }
            override fun onFailure(call: Call<Class_studentResponse>, t: Throwable) {
                hideProgress()
//                ToastUtils.showErrorCustomToast(requireContext(), "Response Failed")
            }
        })
    }

    private fun callclasses() {
//        showProgress()
        var loginApiRequest = TeacherAccessReq( teacherId,auth_token )
        Log.d("classReq", loginApiRequest.toString())
        var call: Call<ClassResponse> = parentApiService!!.classes(loginApiRequest)
        call.enqueue(object : Callback<ClassResponse> {
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()

                        class_names.clear()
                        class_ids.clear()
//                        class_names.add("Select Class")
//                        class_ids.add("0")

                        for(data in loginApiResponse.response.classes){
                            class_names.add(data.class_name)
                            class_ids.add(data.id.toString())
                        }

                        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, class_names)
                        binding.spClass.adapter = adapter1

                    } else {
                        hideProgress()
//                        ToastUtils.showSuccessCustomToast(requireContext(), loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            activity!!.finish()
                        }else{

                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(requireContext(), "Failed")
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(requireContext(), t.message.toString())
            }
        })
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format and display the selected date in the EditText
                val formattedDate = formatDate(selectedDay, selectedMonth + 1, selectedYear)
                binding.etDob.text = formattedDate
            },
            year,
            month,
            day
        )

        // Restrict the calendar to prevent future dates
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

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
    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(context)
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
                intent.resolveActivity(requireActivity().packageManager)?.also {
                    resultLauncher.launch(intent)
                }
            }
        }
    }
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            this?.let {
                intent.resolveActivity(requireActivity().packageManager)?.also {
                    resultLaunchergallery.launch(intent)
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
    private fun callBottomSheet1(studentId: String, oldRemarks: String,) {
        val dialog = Dialog(requireActivity(), R.style.Theme_Material_Dialog_Alert)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding1 = StudentRemarksBinding.inflate(layoutInflater)
        dialog.setContentView(binding1.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        dialog.window!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        dialog.show()
        dialog.window!!.attributes = lp

        binding1.etRemark.setText(oldRemarks)

        binding1.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding1.crossIv.setOnClickListener {
            dialog.dismiss()
        }

        binding1.saveBtn.setOnClickListener {
            if (binding1.etRemark.text.toString() == ""){
                ToastUtils.showSuccessCustomToast(requireContext(), "Enter Remark")
            }else {
                val updateReq = DairyStudentUpdateReq("","",auth_token,
                    classId.toString(),currentDate.toString(),binding.etDetails.text.toString(),
                    binding1.etRemark.text.toString(),studentId,teacherId,checkType)
                    callUpdateReparks(updateReq,dialog)
            }
        }
    }
    private fun callMVVM(request: DairyStudentsReq) {
        showProgress()
        Log.d("request_2024",request.toString())
        viewModel.fetchStudentRemarks(request).observe(requireActivity(), Observer { response ->
            if (response != null && response.status == true ) {
                hideProgress()

              studentsCount =  response.response.students.size.toString()

                binding.dairyRv.visibility = View.VISIBLE
                binding.nodataTv.visibility = View.GONE
                adapter = DairiesNewAdapter(requireActivity(),response.response.students ?: emptyList())
                binding.dairyRv.adapter = adapter
                var layoutManager = LinearLayoutManager(requireContext())
                binding.dairyRv.layoutManager = layoutManager

                adapter.OnItemBtn = {
                        mydata ->
                    val et_details = binding.etDetails.text.toString()
                    if (checkType == ""|| checkType == null){
                        ToastUtils.showSuccessCustomToast(requireContext(), "Select Work Type")
                    }
//                    else if (binding.etDetails.text.toString() == ""||binding.etDetails.text.toString() == null){
//                        ToastUtils.showSuccessCustomToast(requireContext(), "Enter Details")
//                    }
                    else {
                         studentId = mydata.id.toString()
                        val oldremark = mydata.remarks.toString()
                        callBottomSheet1(studentId,oldremark)
                    }
                }

                adapter.OnItemCallPic = {
                        mydata ->
                    val et_details = binding.etDetails.text.toString()
                    if (checkType == ""|| checkType == null){
                        ToastUtils.showSuccessCustomToast(requireContext(), "Select Work Type")
                    }else if (binding.etDetails.text.toString() == ""||binding.etDetails.text.toString() == null){
                        ToastUtils.showSuccessCustomToast(requireContext(), "Enter Details")
                    }else {
                         studentId = mydata.id.toString()
                          oldRemarks = mydata.remarks.toString()
                        selectImage()
//                        callBottomSheet2(studentId,et_details)
                    }
                }

            } else {
                hideProgress()
                binding.dairyRv.visibility = View.GONE
                binding.nodataTv.visibility = View.VISIBLE
                Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun callUpdateReparks(updateReq: DairyStudentUpdateReq, dialog: Dialog) {
        showProgress()
        Log.d("updateReq",updateReq.toString())
        viewModel.dairyStudentUpdate(updateReq).observe(requireActivity(), Observer { response ->
            if (response != null && response.status == true ) {
                hideProgress()
                studentId = ""
                Log.d("updateResponse_2025",response.toString())
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                binding.etDetails.setText("")
                var loginApiRequest = DairyStudentsReq( auth_token,classId.toString(),binding.etDob.text.toString(),teacherId,checkType)
                callMVVM(loginApiRequest)
                dialog.dismiss()
            } else {
                hideProgress()
                Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
            }
        })
    }



}