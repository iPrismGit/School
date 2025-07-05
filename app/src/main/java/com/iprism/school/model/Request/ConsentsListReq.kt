package com.iprism.school.model.Request

data class ConsentsListReq(
    val auth_token: String,
    val date: String,
    val school_id: String,
    val teacher_id: String
)