package com.iprism.school.model.Request

data class GroupDetailsReq(
    val auth_token: String,
    val group_id: String,
    val school_id: String,
    val teacher_id: String
)