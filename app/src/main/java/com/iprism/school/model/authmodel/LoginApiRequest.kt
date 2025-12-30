package com.iprism.school.model.authmodel

data class LoginApiRequest(

    val mobile: String,
    val otp_status: String,
    val player_id: String

)