package com.iprism.school.model.Response

data class AboutUsResponse(
    val message: String,
    val response: ResponseAbout,
    val status: Boolean
)

data class ResponseAbout(
    val aboutus: List<Aboutu>,
    val parent: List<Any>
)

data class Aboutu(
    val content: String,
    val created_on: String,
    val delete_status: String,
    val id: String,
    val school_id: String,
    val updated_on: Any
)