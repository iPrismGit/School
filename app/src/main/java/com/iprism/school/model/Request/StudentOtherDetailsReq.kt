package com.iprism.school.model.Request

data class StudentOtherDetailsReq(
    val auth_token: String,
    val cabs: String,
    val child_dentist: String,
    val child_had_has: String,
    val child_mail: String,
    val child_physician: String,
    val child_suffer_from: String,
    val dentist_conatct_no: String,
    val food_allergies: String,
    val groups: String,
    val hospiatl_contact_no: String,
    val medicine_allergies: String,
    val other_allergies: String,
    val physician_conatct_no: String,
    val prefered_hosptal: String,
    val regular_medication: String,
    val school_id: String,
    val security_amount: String,
    val special_condition: String,
    val student_id: String,
    val teacher_id: String
)