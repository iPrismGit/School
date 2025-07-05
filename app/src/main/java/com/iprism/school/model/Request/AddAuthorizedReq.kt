package com.iprism.school.model.Request

data class AddAuthorizedReq(
    val auth_token: String,
    val person_image: String,
    val person_name: String,
    val school_id: String,
    val student_id: String,
    val student_relation: String,
    val teacher_id: String,
    val type: String
)