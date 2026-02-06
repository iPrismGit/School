package com.iprism.school.model.contentpagesmodel

data class ContentPagesApiRequest(

    val auth_token: String,
    val user_id: String,
    val view_type: String

)