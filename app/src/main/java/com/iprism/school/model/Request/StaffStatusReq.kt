package com.iprism.school.model.Request

data class StaffStatusReq(
    val auth_token: String,
    val school_id: String,
    val staff_id: String,
    val teacher_id: String,
    val type: String
)