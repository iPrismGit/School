package com.iprism.school.model.Response

data class AlbumDetailsResponse(
    val message: String,
    val response: ResponseAlbumDetails,
    val status: Boolean
)

data class ResponseAlbumDetails(
    val album_details: List<AlbumDetailList>
)

data class AlbumDetailList(
    val album_content: List<AlbumContentList>,
    val album_count: Int,
    val album_type: String,
    val class_names: String,
    val description: String,
    val group_names: String,
    val id: String,
    val school_id: String,
    val title: String,
    val classes: String,
    val groups: String,
    val type: String
)

data class AlbumContentList(
    val album_id: String,
    val created_on: String,
    val delete_status: String,
    val file_name: String,
    val id: String,
    val updated_on: Any
)