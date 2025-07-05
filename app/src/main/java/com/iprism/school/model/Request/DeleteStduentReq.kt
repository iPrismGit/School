package com.iprism.school.model.Request

data class DeleteStduentReq(
    val auth_token: String,
    val school_id: String,
    val student_id: String,
    val teacher_id: String
)