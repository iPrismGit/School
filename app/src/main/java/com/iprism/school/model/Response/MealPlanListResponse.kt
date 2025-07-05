package com.iprism.school.model.Response

data class MealPlanListResponse(
    val message: String,
    val response: ResponseMaeal,
    val status: Boolean
)

data class ResponseMaeal(
    val mealplanner: List<MealplannerList>
)

data class MealplannerList(
    val created_on: String,
    val date: String,
    val day: String,
    val delete_status: String,
    val id: String,
    val meal_name: String,
    val meal_type: String,
    val remarks: String,
    val school_id: String,
    val updated_on: Any
)