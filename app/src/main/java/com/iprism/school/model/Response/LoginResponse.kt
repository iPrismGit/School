package com.iprism.school.model.Response

data class LoginResponse(
    val message: String,
    val response: Response,
    val status: Boolean
)

data class Response(
    val teacher_details: List<TeacherDetail>
)

data class TeacherDetail(
    val auth_token: String,
    val created_on: String,
    val delete_status: String,
    val employee_class: String,
    val employee_department: String,
    val employee_designation: String,
    val employee_dob: String,
    val employee_email: String,
    val employee_gender: String,
    val employee_id: String,
    val employee_image: String,
    val employee_mobile: String,
    val employee_name: String,
    val employee_password: String,
    val employee_use_designation: String,
    val id: String,
    val qrcode: String,
    val qrcode_id: String,
    val school_id: String,
    val status: String,
    val token: String,
    val updated_on: String
)