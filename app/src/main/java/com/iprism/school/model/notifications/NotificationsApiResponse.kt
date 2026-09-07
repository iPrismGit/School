package com.iprism.parentapp.model.notifications

import com.google.gson.annotations.SerializedName

data class NotificationsApiResponse(

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

	@field:SerializedName("videos")
	val videos: ArrayList<NotificationsItem>
)

data class Pagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("total_pages")
	val totalPages: List<TotalPagesItem>,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class NotificationsItem(

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("read_status")
	val readStatus: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("cat_name")
	val catName: String,

	@field:SerializedName("type")
	val type: String
)
