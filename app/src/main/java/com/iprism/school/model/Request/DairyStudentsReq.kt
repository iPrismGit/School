package com.iprism.school.model.Request

data class DairyStudentsReq(
    val auth_token: String,
    val class_id: String,
    val date: String,
    val teacher_id: String,
    val type: String
)