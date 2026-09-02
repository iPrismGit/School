package com.iprism.parentapp.model.appreview

import com.google.gson.annotations.SerializedName

data class AppReviewRequest(

	@field:SerializedName("issue")
	val issue: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("branch_id")
	val branchId: String,

	@field:SerializedName("student_id")
	val studentId: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("device")
	val device: String,

	@field:SerializedName("rating")
	val rating: Int
)
