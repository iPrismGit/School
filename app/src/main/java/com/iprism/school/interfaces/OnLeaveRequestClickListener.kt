package com.iprism.school.interfaces

interface OnLeaveRequestClickListener {

    fun onItemClick(leaveRequestId : String, status : String)

    fun onViewAttachmentClick(attachmentUrl : String)

}