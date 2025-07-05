package com.iprism.school.model.Request

data class TeacherGroupStudentsReq(
    val auth_token: String,
    val date: String,
    val group_id: String,
    val school_id: String,
    val teacher_id: String,
    val type: String
)