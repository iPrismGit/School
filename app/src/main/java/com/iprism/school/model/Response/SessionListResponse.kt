package com.iprism.school.model.Response

data class SessionListResponse(
    val message: String,
    val response: ResponseSessionList,
    val status: Boolean
)

data class ResponseSessionList(
    val sessions: List<SessionList>
)

data class SessionList(
    val id: String,
    val session_name: String,
    val session_status: String
)