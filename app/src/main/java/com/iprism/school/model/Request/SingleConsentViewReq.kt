package com.iprism.school.model.Request

data class SingleConsentViewReq(
    val auth_token: String,
    val consent_id: String,
    val date: String,
    val school_id: String,
    val teacher_id: String
)