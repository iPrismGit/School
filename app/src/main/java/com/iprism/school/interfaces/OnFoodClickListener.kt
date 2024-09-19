package com.iprism.school.interfaces

interface OnFoodClickListener {

    fun onFoodItemClick(foodId: String, foodName : String, remarks : String)

    fun onFoodInfoClick(foodId: String)

}