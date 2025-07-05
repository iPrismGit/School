package com.iprism.school.model.Request

data class DaycareActivityIconUpdateReq(
    val activity_icon_id: String,
    val activity_id: String,
    val auth_token: String,
    val school_id: String,
    val teacher_id: String
)