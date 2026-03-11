package com.iprism.school.model.messagemodel

data class DayCareMessagesApiRequest(

    val branch_id: String,
    val daycare_cat_id: String,
    val image: String,
    val message: String,
    val message_type: String,
    val page: String,
    val sender_type: String,
    val student_id: String,
    val thread_id: String,
    val user_id: String,
    val view_type: String

)