package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.activities.EditStaffDetailsActivity
import com.iprism.school.adapters.StaffAdapter
import com.iprism.school.databinding.ActivityCreateStaffBinding
import com.iprism.school.databinding.DeactivateStaffDialogBinding
import com.iprism.school.databinding.FragmentStaffActiveBinding
import com.iprism.school.interfaces.OnStaffClickListener
import com.iprism.school.utils.ToastUtils

class StaffActiveFragment : Fragment() {

    private lateinit var binding: FragmentStaffActiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffActiveBinding.inflate(inflater, container, false)
        setupStaffAdapter()
        return binding.root
    }

    private fun setupStaffAdapter() {
        var staffAdapter = StaffAdapter(requireContext())
        binding.activeStaffRv.adapter = staffAdapter
        var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.activeStaffRv.layoutManager = linearLayoutManager
        staffAdapter.setListener(object : OnStaffClickListener{
            override fun onItemClick() {
                var intent = Intent(requireContext(), EditStaffDetailsActivity::class.java)
                startActivity(intent)
            }

            override fun onDeActiveClick() {
                showDeActiveDialog()
            }

            override fun onReActiveClick() {

            }

            override fun onCallClick() {

            }

        })
    }

    private fun showDeActiveDialog() {
        val binding = DeactivateStaffDialogBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(binding.root)
        val dialog = dialogBuilder.create()
       // dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        binding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        binding.yesBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(requireContext(), "User Deactivated Successfully!")
            dialog.dismiss()
        })
        dialog.show()
    }

}