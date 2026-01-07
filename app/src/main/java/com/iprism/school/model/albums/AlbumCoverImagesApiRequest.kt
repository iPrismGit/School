package com.iprism.school.model.albums

data class AlbumCoverImagesApiRequest(

    val academic_year: String,
    val branch_id: String,
    val class_id: String,
    val date: String,
    val description: String,
    val image: String,
    val page: Int,
    val section_id: String,
    val title: String,
    val user_id: String,
    val view_type: String

)