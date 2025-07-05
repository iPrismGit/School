package com.iprism.school.model.Request

data class InboxMessageReplyReq(
    val attachment: String,
    val auth_token: String,
    val message_id: String,
    val message_type: String,
    val school_id: String,
    val teacher_id: String,
    val reply_message: String
)