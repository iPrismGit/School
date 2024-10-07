package com.iprism.school.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.FoodItemsAdapter
import com.iprism.school.adapters.FoodTypesAdapter
import com.iprism.school.databinding.ActivityMealPlannerBinding
import com.iprism.school.interfaces.OnFoodClickListener
import java.util.Locale

class MealPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealPlannerBinding
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var crossIv: ImageView
    private lateinit var remarkstxt: TextView
    private lateinit var okBtn: Button
    private var foodType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDate()
        setupFoodsAdapter()
        setupFoodTypesAdapter()
        handleBack()
        handleAddBtn()
        hanldeLeftBtn()
        handleRightBtn()
    }

    private fun setDate() {
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
        val formattedDateString: String = sdfString.format(calendar.time)
        Log.d("dateFormatString", formattedDateString)
        binding.dateTxt.text = formattedDate
    }

    private fun hanldeLeftBtn() {
        binding.leftArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(-1)
        })
    }

    private fun handleRightBtn() {
        binding.rightArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(1)
        })
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        binding.dateTxt.text = dateFormat.format(calendar.time)
        var dateFormatString = simpleDateFormat.format(calendar.time)
        Log.d("dateFormatString", dateFormatString)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAddBtn() {
        binding.addFoodBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateMealActivity::class.java))
        })
    }

    private fun setupFoodTypesAdapter() {
        var foodTypesAdapter = FoodTypesAdapter(this)
        binding.foodTypesRv.adapter = foodTypesAdapter
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.foodTypesRv.layoutManager = layoutManager
    }

    private fun setupFoodsAdapter() {
        var foodItemsAdapter = FoodItemsAdapter(this)
        binding.foodsRv.adapter = foodItemsAdapter
        var layoutManager = LinearLayoutManager(this)
        binding.foodsRv.layoutManager = layoutManager
        foodItemsAdapter.setListener(object : OnFoodClickListener {

            override fun onFoodItemClick(foodId: String, foodName: String, remarks: String) {
                val intent = Intent(this@MealPlannerActivity, EditMealPlannerActivity::class.java)
                intent.putExtra("foodId", foodId)
                intent.putExtra("foodName", "")
                intent.putExtra("remarks", "")
                startActivity(intent)
            }

            override fun onFoodInfoClick(foodId: String) {

                showFoodDetailsBottomSheet(foodId)
            }
        })
    }

    private fun showFoodDetailsBottomSheet(foodId: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.food_information_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        crossIv = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        remarkstxt = bottomSheetDialog.findViewById<View>(R.id.remarks_txt) as TextView
        okBtn = bottomSheetDialog.findViewById<View>(R.id.ok_btn) as Button
        remarkstxt.text = "Food " + foodId
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}