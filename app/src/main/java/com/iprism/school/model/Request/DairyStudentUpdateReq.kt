package com.iprism.school.model.Request

data class DairyStudentUpdateReq(
    val attachment: String,
    val attachment_type: String,
    val auth_token: String,
    val class_id: String,
    val date: String,
    val details: String,
    val remarks: String,
    val student_id: String,
    val teacher_id: String,
    val type: String
)