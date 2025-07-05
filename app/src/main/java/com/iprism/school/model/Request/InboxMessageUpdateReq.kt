package com.iprism.school.model.Request

data class InboxMessageUpdateReq(
    val auth_token: String,
    val inbox_message_from: String,
    val inbox_message_id: String,
    val inbox_message_status: String,
    val school_id: String,
    val teacher_id: String
)