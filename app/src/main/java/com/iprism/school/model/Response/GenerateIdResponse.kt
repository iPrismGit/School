package com.iprism.school.model.Response

data class GenerateIdResponse(
    val message: String,
    val response: ResponseIdGenete,
    val status: Boolean
)

data class ResponseIdGenete(
    val admission_id: String
)