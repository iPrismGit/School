package com.iprism.school.model.Request

data class ClassDetailsReq(
    val auth_token: String,
    val class_id: String,
    val school_id: String,
    val teacher_id: String
)