package com.iprism.school.model.Request

data class SchoolStaffReq(
    val auth_token: String,
    val school_id: String,
    val teacher_id: String
)