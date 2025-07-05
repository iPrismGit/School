package com.iprism.school.model.Request

data class StaffDetailsReq(
    val auth_token: String,
    val school_id: String,
    val staff_id: String,
    val staff_type: String,
    val teacher_id: String
)