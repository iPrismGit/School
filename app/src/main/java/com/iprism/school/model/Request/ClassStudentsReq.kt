package com.iprism.school.model.Request

data class ClassStudentsReq(
    val auth_token: String,
    val class_id: String,
    val date: String,
    val school_id: String,
    val teacher_id: String
)