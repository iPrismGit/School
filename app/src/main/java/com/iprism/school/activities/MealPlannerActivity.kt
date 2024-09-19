package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.FoodItemsAdapter
import com.iprism.school.adapters.FoodTypesAdapter
import com.iprism.school.databinding.ActivityMealPlannerBinding

class MealPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealPlannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFoodsAdapter()
        setupFoodTypesAdapter()
    }

    private fun setupFoodTypesAdapter() {
        var foodTypesAdapter = FoodTypesAdapter(this)
        binding.foodTypesRv.adapter = foodTypesAdapter
        var  layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.foodTypesRv.layoutManager = layoutManager
    }

    private fun setupFoodsAdapter() {
        var foodItemsAdapter = FoodItemsAdapter(this)
        binding.foodsRv.adapter = foodItemsAdapter
        var layoutManager = LinearLayoutManager(this)
        binding.foodsRv.layoutManager = layoutManager
    }

}