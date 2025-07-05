package com.iprism.school.model.Request

data class StaffAttandanceReq(
    val attendance_type: String,
    val auth_token: String,
    val date: String,
    val school_id: String,
    val teacher_id: String
)