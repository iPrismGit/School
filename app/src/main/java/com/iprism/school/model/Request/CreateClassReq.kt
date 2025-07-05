package com.iprism.school.model.Request

data class CreateClassReq(
    val auth_token: String,
    val class_name: String,
    val class_section: String,
    val class_teacher_ids: String,
    val school_id: String,
    val session_id: String,
    val teacher_id: String
)