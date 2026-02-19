package com.iprism.school.model.leaverequestmodel

data class LeaveRequestApiRequest(

    val branch_id: String,
    val class_id: String,
    val request_id: String,
    val section_id: String,
    val status: String,
    val user_id: String,
    val view_type: String,
    val reject_reason: String

)