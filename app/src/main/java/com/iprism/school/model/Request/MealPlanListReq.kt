package com.iprism.school.model.Request

data class MealPlanListReq(
    val auth_token: String,
    val date: String,
    val meal_type: String,
    val school_id: String,
    val teacher_id: String
)