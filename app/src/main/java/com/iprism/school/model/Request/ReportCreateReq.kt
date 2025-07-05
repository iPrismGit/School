package com.iprism.school.model.Request

data class ReportCreateReq(
    val message: String,
    val response: ResponseReportCreate,
    val status: Boolean
)

class ResponseReportCreate