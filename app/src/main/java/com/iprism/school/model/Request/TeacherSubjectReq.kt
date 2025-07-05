package com.iprism.school.model.Request

data class TeacherSubjectReq(
    val auth_token: String,
    val school_id: String,
    val subject_id: String,
    val teacher_id: String)