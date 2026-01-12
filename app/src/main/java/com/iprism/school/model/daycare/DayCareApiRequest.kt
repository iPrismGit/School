package com.iprism.school.model.daycare

data class DayCareApiRequest(

    val academic_year: String,
    val activity1: String,
    val activity2: String,
    val branch_id: String,
    val cat_id: String,
    val image: String,
    val message: String,
    val page: Int,
    val student_id: String,
    val time: String,
    val user_id: String,
    val view_type: String,
    val end_time: String

)