package com.iprism.school.model.messagemodel

import com.iprism.school.model.classteachermodel.Pagination

data class MessagesApiResponse(

    val message: String,
    val response: MessagesResponse,
    val status: Boolean

)

data class MessagesResponse (

    val message_threads: List<MessageThread>,
    val pagination: Pagination

)

data class MessageThread(

    val allow_reply: String,
    val class_name: String,
    val first_name: String,
    val id: String,
    val image: String,
    val last_name: String,
    val message: String,
    val message_id: String,
    val message_type: String,
    val date: String,
    val middle_name: String,
    val read_status: String,
    val section_name: String,
    val student_image: String

)
