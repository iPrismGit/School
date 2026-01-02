package com.iprism.school.model.refreshtokenmodel

data class RefreshTokenApiResponse(

    val message: String,
    val response: RefreshTokenResponse,
    val status: Boolean

)

data class RefreshTokenResponse(

    val token: String

)