package com.iprism.school.model.Response

data class DairyUpdateResponse(
    val message: String,
    val response: List<Any>,
    val status: Boolean
)