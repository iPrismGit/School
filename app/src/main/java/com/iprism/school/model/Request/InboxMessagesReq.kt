package com.iprism.school.model.Request

data class InboxMessagesReq(
    val auth_token: String,
    val inbox_message_type: String,
    val messages_type: String,
    val school_id: String,
    val sent_message_id: String,
    val teacher_id: String
)