package com.iprism.school.model.leaverequestmodel

data class LeaveRequestApiResponse(

    val message: String,
    val response: LeaveRequestResponse,
    val status: Boolean

)

data class LeaveRequestResponse(

    val requests: List<Request>,
    val count: Int

)

data class Request(

    val from_date: String,
    val id: String,
    val image: String,
    val name: String,
    val reason: String,
    val status: String,
    val student_id: String,
    val reject_reason: String,
    val to_date: String

)