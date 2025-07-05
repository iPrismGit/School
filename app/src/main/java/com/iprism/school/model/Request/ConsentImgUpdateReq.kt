package com.iprism.school.model.Request

data class ConsentImgUpdateReq(
    val attachment: String,
    val attachment_type: String,
    val auth_token: String,
    val consent_id: String,
    val school_id: String,
    val teacher_id: String
)