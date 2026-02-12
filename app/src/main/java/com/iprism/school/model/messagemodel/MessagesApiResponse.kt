package com.iprism.school.model.messagemodel

import com.google.gson.annotations.SerializedName
import com.iprism.school.model.classteachermodel.Pagination

data class MessagesApiResponse(

    val message: String,
    val response: MessagesResponse,
    val status: Boolean

)

data class MessagesResponse (

    val message_threads: List<MessageThread>,
    val pagination: Pagination,
    val messages: List<MessagesItem>

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

data class MessagesItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("created_on")
    val createdOn: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("sender_type")
    val senderType: String,

    @field:SerializedName("read_status")
    val readStatus: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("message")
    val message: String
)
