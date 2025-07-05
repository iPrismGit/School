package com.iprism.school.model.Response

data class StudentDeleteResponse(
    val message: String,
    val response: ResponseDelete,
    val status: Boolean
)

class ResponseDelete