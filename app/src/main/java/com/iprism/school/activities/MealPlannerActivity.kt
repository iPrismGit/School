package com.iprism.school.activities

import android.content.Intent
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

class MealPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealPlannerBinding
    private lateinit var crossIv : ImageView
    private lateinit var remarkstxt : TextView
    private lateinit var okBtn : Button
    private  var foodType : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFoodsAdapter()
        setupFoodTypesAdapter()
        handleAddBtn()
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

    private fun showFoodDetailsBottomSheet(foodId : String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.food_information_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        crossIv = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        remarkstxt = bottomSheetDialog.findViewById<View>(R.id.remarks_txt) as TextView
        okBtn = bottomSheetDialog.findViewById<View>(R.id.ok_btn) as Button
        remarkstxt.text = "Food " + foodId
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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