package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityPendingRequestDetailsBinding
import com.iprism.school.databinding.ChildHasHadBottomSheetBinding
import com.iprism.school.databinding.PendingRequestItemBinding
import com.iprism.school.databinding.RequestRejectionBottomDialogBinding
import com.iprism.school.utils.ToastUtils

class PendingRequestDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingRequestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleRejectBtn()
        handleBack()
        handleApproveBtn()
    }

    private fun handleApproveBtn() {
        binding.approveBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Request Approved Successfully")
            finish()
        })
    }

    private fun handleRejectBtn() {
        binding.rejectBtn.setOnClickListener(View.OnClickListener {
            showRejectionBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showRejectionBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val rejectionBinding = RequestRejectionBottomDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(rejectionBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            rejectionBinding.rejectBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Rejection Successfully Completed")
                finish()
            })

            rejectionBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            rejectionBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}