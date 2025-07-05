package com.iprism.school.model.Response

data class AdmissionResponse(
    val message: String,
    val response: ResponseAdmissionId,
    val status: Boolean
)

data class ResponseAdmissionId(
    val admission_id: String
)