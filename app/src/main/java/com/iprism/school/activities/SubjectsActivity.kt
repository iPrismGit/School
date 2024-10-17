package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.SubjectsAdapter
import com.iprism.school.databinding.ActivityEditRightsBinding
import com.iprism.school.databinding.ActivitySubjectsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.AddSubjectBottomSheetBinding
import com.iprism.school.databinding.EditSubjectBottomSheetBinding
import com.iprism.school.interfaces.OnSubjectClickListener
import com.iprism.school.utils.ToastUtils

class SubjectsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSubjectsAdapter()
        handleAddBtn()
        handleMoreIv()
    }

    private fun handleMoreIv() {
        binding.moreIv.setOnClickListener(View.OnClickListener {
            showPopupMenu(it)
        })
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            showAddSubjectBottomSheet()
        })
    }

    private fun showAddSubjectBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val subjectBinding = AddSubjectBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(subjectBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            subjectBinding.submitBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Subject Added Successfully")
            })

            subjectBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            subjectBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun setupSubjectsAdapter() {
        var  subjectsAdapter = SubjectsAdapter(this)
        binding.subjectsRv.adapter = subjectsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.subjectsRv.layoutManager = linearLayoutManager
        subjectsAdapter.setupListener(object : OnSubjectClickListener{
            override fun onItemClick(id: String) {
                showEditSubjectBottomSheet()
            }

        })
    }

    private fun showEditSubjectBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val editBinding = EditSubjectBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(editBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            editBinding.submitBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Subject Edited Successfully")
            })

            editBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            editBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.class_subjects_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.class_subjects_lo -> {
                    startActivity(Intent(this, ClassSubjectsActivity::class.java))
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

}