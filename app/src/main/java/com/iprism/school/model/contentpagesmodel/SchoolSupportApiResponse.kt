package com.iprism.school.model.contentpagesmodel

data class SchoolSupportApiResponse(

    val message: String,
    val response: Response,
    val status: Boolean

)

data class Response(

    val address: String,
    val alternate_mobile: String,
    val email: String,
    val lat: String,
    val lon: String,
    val mobile: String

)