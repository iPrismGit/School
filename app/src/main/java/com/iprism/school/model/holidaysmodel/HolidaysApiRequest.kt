package com.iprism.school.model.holidaysmodel

data class HolidaysApiRequest(

    val academic_year: String,
    val branch_id: String,
    val month: String,
    val user_id: String,
    val year: String

)