package com.iprism.school.model.Request

data class ConsentImgDeleteReq(
    val attachment_id: String,
    val auth_token: String,
    val consent_id: String,
    val school_id: String,
    val teacher_id: String
)