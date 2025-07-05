package com.iprism.school.model.Request

data class CLass_StudentsReq(
    val auth_token: String,
    val class_id: String,
    val student_type: String,
    val teacher_id: String
)