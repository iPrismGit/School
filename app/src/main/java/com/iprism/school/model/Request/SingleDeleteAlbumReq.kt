package com.iprism.school.model.Request

data class SingleDeleteAlbumReq(
    val album_content_id: String,
    val album_id: String,
    val auth_token: String,
    val school_id: String,

    val teacher_id: String
)