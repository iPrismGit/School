package com.iprism.school.model.Request

class AllclassesReq (
    val auth_token: String,
    val class_name: String,
    val class_teacher: String,
    val school_id: String,
    val session_id: String,
    val teacher_id: String,
    val type: String
)