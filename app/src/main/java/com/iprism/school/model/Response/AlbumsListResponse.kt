package com.iprism.school.model.Response

data class AlbumsListResponse(
    val message: String,
    val response: ResponseAlbumsList,
    val status: Boolean
)

data class ResponseAlbumsList(
    val album_details: List<AlbumDetail>
)

data class AlbumDetail(
    val album_content: List<AlbumContent>,
    val album_count: Int,
    val album_type: String,
    val id: String,
    val title: String
)

data class AlbumContent(
    val album_id: String,
    val created_on: String,
    val delete_status: String,
    val file_name: String,
    val id: String,
    val updated_on: Any
)