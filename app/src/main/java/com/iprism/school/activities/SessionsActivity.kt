package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.SessionsAdapter
import com.iprism.school.databinding.ActivitySessionsBinding
import com.iprism.school.databinding.AddSessionBottomSheetBinding
import com.iprism.school.databinding.AddSubjectBottomSheetBinding
import com.iprism.school.databinding.ChildSuffersFromBottomSheetBinding
import com.iprism.school.databinding.ClassStudentItemBinding
import com.iprism.school.databinding.UpdateSessionBottomSheetBinding
import com.iprism.school.interfaces.OnSessionClickListener
import com.iprism.school.utils.ToastUtils

class SessionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupSessionsAdapter()
        handleAddSessionBtn()
    }

    private fun handleAddSessionBtn() {
        binding.addSessionBtn.setOnClickListener(View.OnClickListener {
            showAddSessionBottomSheet()
        })
    }

    private fun setupSessionsAdapter() {
        var sessionsAdapter = SessionsAdapter(this)
        binding.sessionsRv.adapter = sessionsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.sessionsRv.layoutManager = linearLayoutManager
        sessionsAdapter.setListener(object : OnSessionClickListener{
            override fun onItemClick(sessionId: String) {
                showUpdateSessionBottomSheet()
            }

        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showAddSessionBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = AddSessionBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.createBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Session Added Successfully")
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showUpdateSessionBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = UpdateSessionBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.updateBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Session Updated Successfully")
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}