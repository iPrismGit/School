package com.iprism.school.model.Request

data class SendFeedBackReq(
    val attachment: String,
    val auth_token: String,
    val rating: String,
    val remarks: String,
    val school_id: String,
    val suggestion: String,
    val teacher_id: String
)