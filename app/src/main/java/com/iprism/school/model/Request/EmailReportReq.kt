package com.iprism.school.model.Request

data class EmailReportReq(
    val auth_token: String,
    val email: String,
    val from_date: String,
    val school_id: String,
    val teacher_id: String,
    val to_date: String
)