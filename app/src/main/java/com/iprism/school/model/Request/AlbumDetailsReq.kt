package com.iprism.school.model.Request

data class AlbumDetailsReq(
    val album_id: String,
    val auth_token: String,
    val school_id: String,
    val teacher_id: String
)