package com.iprism.school.model.Request

data class MarkAllReadReq(
    val auth_token: String,
    val inbox_messages: String,
    val school_id: String,
    val teacher_id: String
)

data class InboxMessage(
    val from: String,
    val id: String
)