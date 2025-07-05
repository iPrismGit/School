package com.iprism.school.model.Request

data class SingleAlbumAddReq(
    val album_id: String,
    val album_type: String,
    val attachments: String,
    val auth_token: String,
    val school_id: String,
    val teacher_id: String
)