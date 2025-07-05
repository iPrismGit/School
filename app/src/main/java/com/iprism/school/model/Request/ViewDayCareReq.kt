package com.iprism.school.model.Request

data class ViewDayCareReq(
    val auth_token: String,
    val date: String,
    val school_id: String,
    val student_id: String,
    val teacher_id: String
)