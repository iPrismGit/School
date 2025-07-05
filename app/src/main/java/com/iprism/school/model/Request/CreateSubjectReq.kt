package com.iprism.school.model.Request

data class CreateSubjectReq(
    val auth_token: String,
    val description: String,
    val school_id: String,
    val subject_name: String,
    val subject_type: String,
    val teacher_id: String
)