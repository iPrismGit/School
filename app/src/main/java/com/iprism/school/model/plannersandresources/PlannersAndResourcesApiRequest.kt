package com.iprism.school.model.plannersandresources

data class PlannersAndResourcesApiRequest(

    val academic_year: String,
    val branch_id: String,
    val cat_id: String,
    val page: Int,
    val planner_id: String,
    val user_id: String,
    val view_type: String

)