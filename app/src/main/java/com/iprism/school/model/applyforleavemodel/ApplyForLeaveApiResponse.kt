package com.iprism.school.model.applyforleavemodel

data class ApplyForLeaveApiResponse(

    val message: String,
    val response: ApplyForLeaveResponse,
    val status: Boolean

)

data class ApplyForLeaveResponse(

    val leave_requests: List<LeaveRequest>

)

data class LeaveRequest(

    val from_date: String,
    val id: String,
    val image: String,
    val name: String,
    val reason: String,
    val status: String,
    val to_date: String

)