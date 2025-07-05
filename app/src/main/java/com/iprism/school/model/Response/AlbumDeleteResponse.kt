package com.iprism.school.model.Response

data class AlbumDeleteResponse(
    val message: String,
    val response: List<Any>,
    val status: Boolean
)