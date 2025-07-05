package com.iprism.school.model.Response

data class PrivacyResponse(
    val message: String,
    val response: ResponsePrivacy,
    val status: Boolean
)

data class ResponsePrivacy(
    val parent: List<Any>,
    val privacypolicy: List<Privacypolicy>
)

data class Privacypolicy(
    val content: String,
    val created_on: String,
    val delete_status: String,
    val id: String,
    val school_id: String,
    val updated_on: Any
)