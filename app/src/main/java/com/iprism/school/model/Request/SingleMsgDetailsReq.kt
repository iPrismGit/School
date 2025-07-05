package com.iprism.school.model.Request

data class SingleMsgDetailsReq(
    val auth_token: String,
    val message_type: String,
    val school_id: String,
    val sent_message_id: String,
    val teacher_id: String
)