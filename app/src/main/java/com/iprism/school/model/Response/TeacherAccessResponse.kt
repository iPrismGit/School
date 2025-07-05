package com.iprism.school.model.Response

data class TeacherAccessResponse(
    val message: String,
    val response: ResponseTeacherAccess,
    val status: Boolean
)

data class ResponseTeacherAccess(
    val access_details: List<AccessDetail>
)

data class AccessDetail(
    val add_edit_cab_details: String,
    val add_edit_staff_details: String,
    val add_edit_student_details: String,
    val collect_fees: String,
    val create_albums: String,
    val create_inquiry_registration: String,
    val created_on: String,
    val delete_status: String,
    val delete_transactions: String,
    val edit_inquiry_registration: String,
    val expense: String,
    val gatepass_reports: String,
    val id: String,
    val library: String,
    val manage_exam: String,
    val manage_fees: String,
    val school_id: String,
    val school_page: String,
    val send_communications: String,
    val set_as_admin: String,
    val set_as_management: String,
    val staff_id: String,
    val timetable: String,
    val updated_on: Any,
    val view_reports: String,
    val view_students: String,
    val visitor_management: String
)