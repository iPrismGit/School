package com.iprism.school.model.Response

data class CreateStudentResponse(
    val message: String,
    val response: ResponseCreateStdent,
    val status: Boolean
)

data class ResponseCreateStdent(
    val student_id: Int
)