package com.iprism.school.interfaces

interface OnMessageClickListener {

    fun onItemClick(messageId : String)

    fun onStudentSelectClick(value : String, studentId : String, studentName : String)

}