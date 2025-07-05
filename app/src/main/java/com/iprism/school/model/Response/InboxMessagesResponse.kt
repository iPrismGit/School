package com.iprism.school.model.Response

data class InboxMessagesResponse(
    val message: String,
    val response: ResponseInBox,
    val status: Boolean
)

data class ResponseInBox(
    val inbox_messages: List<InboxMessageList>
)

data class InboxMessageList(
    val archived_message: String,
    val date: String,
    val inbox_message_from: String,
    val message: String,
    val message_id: String,
    val name: String,
    val image: String,
    val read_message: String,
    val starred_message: String,
    val subject: String
)