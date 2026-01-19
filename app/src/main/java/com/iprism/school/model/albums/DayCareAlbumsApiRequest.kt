package com.iprism.school.model.albums

data class DayCareAlbumsApiRequest(

    val academic_year: String,
    val branch_id: String,
    val cat_id: String,
    val date: String,
    val description: String,
    val image: String,
    val page: Int,
    val title: String,
    val user_id: String,
    val view_type: String

)