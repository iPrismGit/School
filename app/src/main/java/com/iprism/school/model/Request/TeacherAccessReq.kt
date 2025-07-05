package com.iprism.school.model.Request

data class TeacherAccessReq (
    val teacher_id: String,
    val auth_token: String
)