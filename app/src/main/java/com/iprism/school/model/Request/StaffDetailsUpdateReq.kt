package com.iprism.school.model.Request

data class StaffDetailsUpdateReq(
    val auth_token: String,
    val date_of_joining: String,
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
    val employee_use_designation: String,
    val school_id: String,
    val staff_id: String,
    val teacher_id: String
)