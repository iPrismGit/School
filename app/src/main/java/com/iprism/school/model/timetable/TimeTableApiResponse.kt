package com.iprism.school.model.timetable

import com.google.gson.annotations.SerializedName

data class TimeTableApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class TotalPagesItem(

	@field:SerializedName("page")
	val page: Int
)

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("time_table")
	val timeTable: List<TimeTableItem>
)

data class Pagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("total_pages")
	val totalPages: List<TotalPagesItem>,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class TimeTableItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("id")
	val id: Int
)
