package com.iprism.school.model.Request

data class CreateMealReq(
    val auth_token: String,
    val date: String,
    val meal_name: String,
    val meal_type: String,
    val remarks: String,
    val school_id: String,
    val teacher_id: String
)