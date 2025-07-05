package com.iprism.school.model.Response

data class DayCareReportsStudentsResponse(
    val message: String,
    val response: ResponseReportStiudents,
    val status: Boolean
)

data class ResponseReportStiudents(
    val groups: List<GroupStuuu>
)

data class GroupStuuu(
    val has_daycare_report: String,
    val id: String,
    val student_image: String,
    val student_name: String
)