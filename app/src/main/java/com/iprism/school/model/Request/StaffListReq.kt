package com.iprism.school.model.Request

data class StaffListReq(
    val auth_token: String,
    val school_id: String,
    val search_key: String,
    val staff_type: String,
    val teacher_id: String,
    val type: String
)