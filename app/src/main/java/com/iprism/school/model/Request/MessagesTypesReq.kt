package com.iprism.school.model.Request

data class MessagesTypesReq(
    val auth_token: String,
    val message_type: String,
    val school_id: String,
    val teacher_id: String
)