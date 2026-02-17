package com.iprism.school.model.messagemodel

data class MessagesApiRequest(

    val academic_year: String,
    val branch_id: String,
    val class_id: String,
    val image: String,
    val message: String,
    val message_type: String,
    val page: String,
    val section_id: String,
    val sender_type: String,
    val student_id: String,
    val thread_id: String,
    val user_id: String,
    val view_type: String

)