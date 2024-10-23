package com.iprism.school.activities

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateAlbumsBinding
import com.iprism.school.databinding.ActivityInformationBinding
import com.iprism.school.databinding.AlbumConfirmationBottomSheetBinding
import com.iprism.school.databinding.ChildHasHadBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class CreateAlbumsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAlbumsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleCreateBtn()
        handleAddBtn()
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            binding.optionsLo.visibility = View.VISIBLE
        })
    }

    private fun handleCreateBtn() {
        binding.createBtn.setOnClickListener(View.OnClickListener {
            blinkButton(binding.createBtn)
            showConfirmationBottomSheet()
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
            finish()
        })
    }

    private fun showConfirmationBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = AlbumConfirmationBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.confirmBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Album Created Successfully")
                finish()
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.noBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}