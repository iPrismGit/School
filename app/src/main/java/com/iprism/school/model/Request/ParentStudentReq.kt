package com.iprism.school.model.Request

data class ParentStudentReq(
    val address: String,
    val auth_token: String,
    val father_email: String,
    val father_image: String,
    val father_mobile: String,
    val father_name: String,
    val father_occupation: String,
    val guardian_mobile: String,
    val guardian_name: String,
    val mother_email: String,
    val mother_image: String,
    val mother_mobile: String,
    val mother_name: String,
    val mother_occupation: String,
    val pincode: String,
    val school_id: String,
    val student_id: String,
    val teacher_id: String
)