package com.iprism.school.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R
import com.iprism.school.adapters.ActivityIconsAdapter
import com.iprism.school.adapters.SetActivityIconsAdapter
import com.iprism.school.databinding.ActivitySetIconBinding
import com.iprism.school.interfaces.OnActivityClickListener
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.utils.ToastUtils

class SetActivityIconActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetIconBinding
    private lateinit var crossImg: ImageView
    private lateinit var activityIconsRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetIconBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupActivityIconsAdapter()
    }

    private fun setupActivityIconsAdapter() {
        var setActivityIconsAdapter = SetActivityIconsAdapter(this)
        binding.activityIconsRv.adapter = setActivityIconsAdapter
        var layoutManager = LinearLayoutManager(this)
        binding.activityIconsRv.layoutManager = layoutManager
        setActivityIconsAdapter.setupListener(object : OnActivityClickListener {
            override fun onItemClick(imageUrl: String, id: String) {
                showActivityIconsDialog()
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun showActivityIconsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_icons_layout, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        crossImg = dialogView.findViewById<View>(R.id.cross_iv) as ImageView
        activityIconsRv = dialogView.findViewById<View>(R.id.activity_icons_rv) as RecyclerView
        var activityIconsAdapter = ActivityIconsAdapter(this@SetActivityIconActivity)
        activityIconsRv.adapter = activityIconsAdapter
        var linearLayout = GridLayoutManager(this, 3)
        activityIconsRv.layoutManager = linearLayout
        crossImg.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        activityIconsAdapter.setupListener(object : OnDayCareClickListener {
            override fun onItemLick(id: String, name: String) {
                ToastUtils.showSuccessCustomToast(
                    this@SetActivityIconActivity, "Activity Icon Changed Successfully"
                )
                dialog.dismiss()
            }
        })

        dialog.show()
    }

}