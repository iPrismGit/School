package com.iprism.school.model.insertfee

import com.google.gson.annotations.SerializedName

data class InsertFeeRequest(

	@field:SerializedName("payment_mode")
	val paymentMode: String,

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("late_fee")
	val lateFee: String,

	@field:SerializedName("cheque_number")
	val chequeNumber: String,

	@field:SerializedName("class_id")
	val classId: String,

	@field:SerializedName("admin_remark")
	val adminRemark: String,

	@field:SerializedName("due_date")
	val dueDate: String,

	@field:SerializedName("student_id")
	val studentId: String,

	@field:SerializedName("remark")
	val remark: String,

	@field:SerializedName("cheque_date")
	val chequeDate: String,

	@field:SerializedName("print_type")
	val printType: String,

	@field:SerializedName("reference_number")
	val referenceNumber: String,

	@field:SerializedName("receipt_number")
	val receiptNumber: String,

	@field:SerializedName("section_id")
	val sectionId: String,

	@field:SerializedName("academic_year")
	val academicYear: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("branch_id")
	val branchId: String,

	@field:SerializedName("bank_name")
	val bankName: String,

	@field:SerializedName("receipt_date")
	val receiptDate: String,

	@field:SerializedName("configuration_id")
	val configurationId: String
)
