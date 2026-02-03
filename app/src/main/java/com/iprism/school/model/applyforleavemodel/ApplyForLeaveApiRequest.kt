package com.iprism.school.model.applyforleavemodel

data class ApplyForLeaveApiRequest(

    val academic_year: String,
    val branch_id: String,
    val from_date: String,
    val image: String,
    val name: String,
    val reason: String,
    val to_date: String,
    val user_id: String,
    val view_type: String

)