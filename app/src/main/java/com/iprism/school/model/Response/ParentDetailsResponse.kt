package com.iprism.school.model.Response

data class ParentDetailsResponse(
    val message: String,
    val response: List<Any>,
    val status: Boolean
)