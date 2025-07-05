package com.iprism.school.model.Request

data class CreateConsentsReq(
    val attachment: String,
    val attachment_type: String,
    val auth_token: String,
    val class_ids: String,
    val date: String,
    val details: String,
    val group_ids: String,
    val school_id: String,
    val student_ids: String,
    val teacher_id: String,
    val time: String,
    val title: String,
    val type: String
)