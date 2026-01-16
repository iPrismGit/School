package com.iprism.school.model.authmodel

data class LoginApiResponse(

    val message: String,
    val response: LoginResponse,
    val status: Boolean

)

data class LoginResponse(

    val alternate_mobile: String,
    val otp: String,
    val auth_token: String,
    val blood_group: String,
    val branch_id: String,
    val branch_name: String,
    val created_on: String,
    val current_address: String,
    val delete_status: Int,
    val designation_id: String,
    val device_token: String,
    val dob: String,
    val doj: String,
    val email: String,
    val emergency_mobile: String,
    val emp_id: String,
    val exp: String,
    val first_name: String,
    val gender: String,
    val home_phone: String,
    val id: String,
    val image: String,
    val ios_version: String,
    val job_title: String,
    val last_name: String,
    val maritial_status: String,
    val middle_name: String,
    val mobile: String,
    val modified_on: String,
    val nationality: String,
    val office_number: String,
    val permanent_address: String,
    val player_id: String,
    val previous_experience: String,
    val salary: String,
    val status: String

)