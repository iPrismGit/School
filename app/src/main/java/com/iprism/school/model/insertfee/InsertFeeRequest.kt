package com.iprism.school.model.insertfee

import com.google.gson.annotations.SerializedName

data class InsertFeeRequest(

	@field:SerializedName("payment_mode")
	val paymentMode: String,

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("late_fee")
	val lateFee: Int,

	@field:SerializedName("cheque_number")
	val chequeNumber: String,

	@field:SerializedName("class_id")
	val classId: Int,

	@field:SerializedName("admin_remark")
	val adminRemark: String,

	@field:SerializedName("due_date")
	val dueDate: String,

	@field:SerializedName("student_id")
	val studentId: Int,

	@field:SerializedName("remark")
	val remark: String,

	@field:SerializedName("cheque_date")
	val chequeDate: String,

	@field:SerializedName("print_type")
	val printType: String,

	@field:SerializedName("reference_number")
	val referenceNumber: String,

	@field:SerializedName("receipt_number")
	val receiptNumber: Int,

	@field:SerializedName("section_id")
	val sectionId: Int,

	@field:SerializedName("academic_year")
	val academicYear: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("branch_id")
	val branchId: Int,

	@field:SerializedName("bank_name")
	val bankName: String,

	@field:SerializedName("receipt_date")
	val receiptDate: String,

	@field:SerializedName("configuration_id")
	val configurationId: Int
)
