package com.iprism.school.model.Response

data class AlbumUploadResponse(
    val message: String,
    val response: List<Any>,
    val status: Boolean
)