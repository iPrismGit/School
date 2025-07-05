package com.iprism.school.model.Request

data class LoginReq(
    val mobile: String,
    val otp_confirmed: String,
    val token: String
)