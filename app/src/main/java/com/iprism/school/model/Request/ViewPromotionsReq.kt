package com.iprism.school.model.Request

data class ViewPromotionsReq(
    val message: String,
    val response: ResponseViewPromotions,
    val status: Boolean
)

data class ResponseViewPromotions(
    val attachments: List<Attachment>,
    val promotion: List<Promotion>
)

data class Attachment(
    val created_on: String,
    val id: String,
    val promotion_id: String,
    val school_id: String,
    val url: String
)

data class Promotion(
    val attachment_type: String,
    val created_on: String,
    val description: String,
    val id: String,
    val school_id: String,
    val staff_id: String,
    val updated_on: String
)