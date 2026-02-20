package com.iprism.school.model.profile

data class ProfileApiResponse(

    val message: String,
    val response: ProfileResponse,
    val status: Boolean

)

data class ProfileResponse(

    val alternate_mobile: String,
    val blood_group: String,
    val branch: String,
    val gender_name: String,
    val class_teacher: List<ClassTeacher>,
    val current_address: String,
    val daycare_categories: List<DaycareCategory>,
    val designation: String,
    val designation_id: String,
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
    val job_title: String,
    val last_name: String,
    val maritial_status: String,
    val middle_name: String,
    val mobile: String,
    val nationality: String,
    val office_number: String,
    val permanent_address: String,
    val previous_experience: String,
    val salary: String,
    val username: String

)

data class ClassTeacher(

    val class_name: String,
    val section_name: String

)

data class DaycareCategory(

    val cat_name: String

)