package com.iprism.school.model.Request

data class TeacherCalederDetailsReq(
    val auth_token: String,
    val calender_id: String,
    val school_id: String,
    val teacher_id: String
)