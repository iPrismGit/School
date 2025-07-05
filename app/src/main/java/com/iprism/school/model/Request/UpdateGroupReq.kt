package com.iprism.school.model.Request

data class UpdateGroupReq(
    val attachment: String,
    val auth_token: String,
    val group_admins: String,
    val group_description: String,
    val group_id: String,
    val group_name: String,
    val group_staff: String,
    val group_students: String,
    val school_id: String,
    val teacher_id: String
)