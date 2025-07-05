package com.iprism.school.model.Request

data class CreateStudentReq(
    val admission_id: String,
    val auth_token: String,
    val caste: String,
    val class_id: String,
    val joining_date: String,
    val nationality: String,
    val religion: String,
    val school_id: String,
    val session_id: String,
    val student_blood_group: String,
    val student_dob: String,
    val student_gender: String,
    val student_image: String,
    val student_name: String,
    val teacher_id: String
)