package com.iprism.school.model.Request

data class CreatePromotionsReq(
    val attachment_type: String,
    val attachments: String,
    val auth_token: String,
    val description: String,
    val school_id: String,
    val teacher_id: String
)