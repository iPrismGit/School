package com.iprism.school.model.Response

data class ReportStudentsResponse(
    val message: String,
    val response: ResponseReportStudents,
    val status: Boolean
)

data class ResponseReportStudents(
    val groups: List<Groupreport>
)

data class Groupreport(
    val has_daycare_report: String,
    val id: String,
    val student_name: String
)