package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityAlbumDetailsBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class AlbumDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailsBinding
    private var albumId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        albumId = intent.getStringExtra("albumId").toString()
        handleBack()
        handleDeleteIv()
        handleMoreBtn()
    }

    private fun handleMoreBtn() {
        binding.moreIv.setOnClickListener(View.OnClickListener {
            showPopupMenu(it)
        })
    }

    private fun handleDeleteIv() {
        binding.deleteIv.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.album_menu_item, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share_lo -> {
                    shareAppLink()
                    true
                }

                R.id.share_on_face_book_lo ->{
                    shareAppLink()
                    true
                }

                R.id.download_all_images_lo ->{
                    ToastUtils.showSuccessCustomToast(this, "Images Downloaded Successfully")
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun shareAppLink() {
        val appLink = "https://play.google.com/store/apps/details?id=" + "com.iprism.school"
        val shareText = "Hey check out beauty services app at $appLink"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, "Share app via"))
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val deleteBinding = DeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(deleteBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }
        deleteBinding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBinding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBinding.deleteButton.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Album Deleted Successfully")
            finish()
        })

        bottomSheetDialog.show()
    }

}