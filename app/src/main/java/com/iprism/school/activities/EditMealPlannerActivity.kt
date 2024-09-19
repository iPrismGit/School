package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.iprism.school.R
import com.iprism.school.databinding.ActivityEditMealPlannerBinding
import com.iprism.school.databinding.ActivityMealPlannerBinding

class EditMealPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMealPlannerBinding
    private var foodId : String = ""
    private var foodName : String = ""
    private var remarks : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMealPlannerBinding.inflate(layoutInflater)
        foodId = intent.getStringExtra("foodId").toString()
        foodName = intent.getStringExtra("foodName").toString()
        remarks = intent.getStringExtra("remarks").toString()
        setContentView(binding.root)
        Log.d("foodId", "$foodId, $foodName, $remarks")
    }

}