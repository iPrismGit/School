package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.iprism.school.base.BaseFragment
import com.iprism.school.R
import com.iprism.school.activities.HomeActivity
import com.iprism.school.databinding.FragmentStudentScannerBinding
import com.iprism.school.databinding.SuccessitemBinding
import com.iprism.school.model.Request.StudentAttandanceUpdateReq
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel

class StudentScannerFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentScannerBinding
    private lateinit var binding1 : SuccessitemBinding


    private var codeScanner: CodeScanner? = null

    private val viewModel: Scl_ViewModel by viewModels()

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var studentId: String = ""

    private var markus: String = ""
    private var sendnotification: String = ""
    private var qrCode: String = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStudentScannerBinding.inflate(inflater, container, false)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()


        binding.toggleIn.setOnClickListener {
            markus = "in"
            binding.toggleIn.background = ContextCompat.getDrawable(requireContext(), R.drawable.left_green)
            binding.toggleOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.right_grey)

            binding.toggleIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.toggleOut.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }


        binding.toggleOut.setOnClickListener {
            markus = "out"
            binding.toggleIn.background = ContextCompat.getDrawable(requireContext(), R.drawable.left_grey)
            binding.toggleOut.background = ContextCompat.getDrawable(requireContext(), R.drawable.right_green)

            binding.toggleIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.toggleOut.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }


        binding.yes.setOnClickListener {
            sendnotification = "yes"
            binding.yes.background = ContextCompat.getDrawable(requireContext(), R.drawable.left_green)
            binding.no.background = ContextCompat.getDrawable(requireContext(), R.drawable.right_grey)

            binding.yes.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.no.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.no.setOnClickListener {
            sendnotification = "no"
            binding.yes.background = ContextCompat.getDrawable(requireContext(), R.drawable.left_grey)
            binding.no.background = ContextCompat.getDrawable(requireContext(), R.drawable.right_green)

            binding.yes.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.no.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        codeScanner = CodeScanner(requireContext(), binding.barcodeScanner)
        // Parameters (default values)
        codeScanner!!.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner!!.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner!!.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner!!.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner!!.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner!!.isFlashEnabled = false // Whether to enable flash or not

        binding.studentscannerID.visibility = View.GONE
        codeScanner!!.decodeCallback = DecodeCallback {
           requireActivity().runOnUiThread {
                if (it.text.toString() ==null||it.text.toString() == ""){
                    binding.studentscannerID.visibility = View.GONE
                }else{
                     qrCode = it.text.toString()
                    ToastUtils.showSuccessCustomToast(requireContext(),"Student ID : "+ it.text)
                    vibratePhone()
                    binding.studentscannerID.text = "Student Scanner Id : "+ qrCode.toString()
                    binding.studentscannerID.visibility = View.VISIBLE
                }
            }
        }

        codeScanner!!.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

//        codeScanner = CodeScanner(requireContext(), binding.barcodeScanner)
//        codeScanner!!.decodeCallback = DecodeCallback {
//            vibratePhone()
//            val intent = Intent(context, HelpTutorialsActivity::class.java)
//            startActivity(intent)
//        }
        binding.barcodeScanner.setOnClickListener {
            codeScanner!!.startPreview()
        }

        binding.submitBtn.setOnClickListener {
            if (qrCode == ""||qrCode == null){
                Toast.makeText(requireContext(), "Scan QR Code", Toast.LENGTH_SHORT).show()
            }else if (markus == ""||markus == null){
                Toast.makeText(requireContext(), "Select Mark Us", Toast.LENGTH_SHORT).show()
            }else if (sendnotification == ""||sendnotification == null){
                Toast.makeText(requireContext(), "Select Send Notifications", Toast.LENGTH_SHORT).show()
            }else{
                val  studentAttandanceUpdate = StudentAttandanceUpdateReq("student",auth_token,markus,
                    qrCode,sendnotification,teacherId)
                studentAttandanceUpdate(studentAttandanceUpdate)
            }
        }

        return binding.root
    }

    private fun vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    

    private fun studentAttandanceUpdate(updateReq: StudentAttandanceUpdateReq) {
        showProgress()
        Log.d("updateReq",updateReq.toString())
        viewModel.studentAttandanceUpdate(updateReq).observe(requireActivity(), Observer { response ->
            if (response != null && response.status == true ) {
                hideProgress()
                Log.d("updateResponse_2025",response.toString())
                callBottomSheet()
            } else {
                hideProgress()
                Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun callBottomSheet() {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Material_Dialog_Alert)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding1 = SuccessitemBinding.inflate(layoutInflater)
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


        binding1.crossIv.setOnClickListener {
            dialog.dismiss()
        }

        binding1.submitBtn.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireContext(),HomeActivity::class.java)
            intent.putExtra("tag","from_qr")
            startActivity(intent)
        }
    }


    private fun refreshFragment() {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }


}