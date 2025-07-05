package com.iprism.school.model.Request

data class CreateAlbumReq(
    val album_type: String,
    val attachments: String,
    val auth_token: String,
    val classes: String,
    val description: String,
    val groups: String,
    val school_id: String,
    val teacher_id: String,
    val title: String,
    val type: String,
    val album_id: String
)