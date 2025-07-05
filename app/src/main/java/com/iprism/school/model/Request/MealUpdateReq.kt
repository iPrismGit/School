package com.iprism.school.model.Request

data class MealUpdateReq(
    val auth_token: String,
    val meal_name: String,
    val meal_planner_id: String,
    val remarks: String,
    val school_id: String,
    val teacher_id: String
)