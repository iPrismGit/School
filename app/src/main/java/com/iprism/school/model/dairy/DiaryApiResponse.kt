package com.iprism.school.model.dairy

import com.iprism.school.model.classteachermodel.Pagination

data class DiaryApiResponse(

    val message: String,
    val response: DiaryResponse,
    val status: Boolean

)

data class DiaryResponse(

    val diaries: List<Diary>,
    val pagination: Pagination

)

data class Diary(

    val details: String,
    val first_name: String,
    val id: Int,
    val image: String,
    val last_name: String,
    val middle_name: String,
    val student_id: String,
    val type: String

)