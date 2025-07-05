package com.iprism.school.model.Response

data class TermsandConditionResponse(
    val message: String,
    val response: Responseterms,
    val status: Boolean
)

data class Responseterms(
    val parent: List<Any>,
    val termsandconditions: List<Termsandcondition>
)

data class Termsandcondition(
    val content: String,
    val created_on: String,
    val delete_status: String,
    val id: String,
    val school_id: String,
    val updated_on: Any
)