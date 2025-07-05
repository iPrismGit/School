package com.iprism.school.model.Request

data class StudentsListReq(
    val auth_token: String,
    val class_id: String,
    val school_id: String,
    val search_key: String,
    val student_type: String,
    val teacher_id: String,
    val type: String
)