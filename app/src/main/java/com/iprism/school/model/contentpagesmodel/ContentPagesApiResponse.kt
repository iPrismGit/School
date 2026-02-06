package com.iprism.school.model.contentpagesmodel

data class ContentPagesApiResponse(

    val message: String,
    val response: ContentPagesResponse,
    val status: Boolean

)

data class ContentPagesResponse(

    val name: String

)