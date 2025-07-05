package com.iprism.school.model.Response

data class SuccessResponsePojo(
    val message: String,
    val response: List<Any>,
    val status: Boolean
)