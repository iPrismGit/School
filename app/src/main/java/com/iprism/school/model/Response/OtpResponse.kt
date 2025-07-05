package com.iprism.school.model.Response

data class OtpResponse(
    val message: String,
    val response: ResponseOtp,
    val status: Boolean
)

data class ResponseOtp(
    val otp: String
)