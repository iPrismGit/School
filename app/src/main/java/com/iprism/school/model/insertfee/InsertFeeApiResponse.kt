package com.iprism.school.model.insertfee

import com.google.gson.annotations.SerializedName

data class InsertFeeApiResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String
)

data class Response(
	val any: Any? = null
)
