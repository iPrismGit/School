package com.iprism.school.model.Response

data class Class_studentResponse(
    val message: String,
    val response: ResponseClassStudents,
    val status: Boolean
)

data class ResponseClassStudents(
    val students: List<StudentList>
)

data class StudentList(
    val address: String,
    val admission_id: String,
    val anniversary_date: String,
    val authorized_person: String,
    val cabs: String,
    val caste: String,
    val city: String,
    val class_id: String,
    val created_on: String,
    val delete_status: String,
    val emergency_contact_contact: String,
    val emergency_contact_person: String,
    val father_annual_income: String,
    val father_dob: String,
    val father_email: String,
    val father_image: String,
    val father_mobile: String,
    val father_name: String,
    val father_occupation: String,
    val father_office_address: String,
    val father_office_designation: String,
    val groups: String,
    val guardian_email: String,
    val guardian_image: String,
    val guardian_mobile: String,
    val guardian_name: String,
    val id: String,
    val joining_date: String,
    val mother_annual_income: String,
    val mother_dob: Any,
    val mother_email: String,
    val mother_image: String,
    val mother_mobile: String,
    val mother_name: String,
    val mother_occupation: String,
    val mother_office_address: String,
    val mother_office_designation: String,
    val pincode: String,
    val qrcode: String,
    val qrcode_id: String,
    val school_id: String,
    val security_amount: String,
    val session_id: String,
    val status: String,
    val student_blood_group: String,
    val student_dob: String,
    val student_gender: String,
    val student_image: String,
    val student_name: String,
    val remarks: String,
    val updated_on: String
)