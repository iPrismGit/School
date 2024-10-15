package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.GroupStudentsAdapter
import com.iprism.school.databinding.ActivityGroupDetailsBinding
import com.iprism.school.databinding.ActivityGroupsBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.databinding.GroupAdminsBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class GroupDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupDetailsBinding
    private var isInfoVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupGroupDetailsAdapter()
        handleStudentsDropDown()
        handleBack()
        handleDeleteBtn()
    }

    private fun handleDeleteBtn() {
        binding.deleteIv.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleStudentsDropDown() {
        binding.arrowLo.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun setupGroupDetailsAdapter() {
        var groupStudentsAdapter = GroupStudentsAdapter(this)
        binding.groupStudentsRv.adapter = groupStudentsAdapter
        var  linearLayoutManager = LinearLayoutManager(this)
        binding.groupStudentsRv.layoutManager = linearLayoutManager
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.studentsLo.visibility = View.GONE
            binding.downArrow.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.studentsLo.visibility = View.VISIBLE
            binding.downArrow.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        binding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.deleteButton.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Group Deleted Successfully")
            finish()
        })

        bottomSheetDialog.show()
    }

}