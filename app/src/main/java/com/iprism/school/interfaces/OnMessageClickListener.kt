package com.iprism.school.interfaces

interface OnMessageClickListener {

    fun onItemClick(threadId : String, name : String, image : String, type : String)

    fun onStudentSelectClick(value : String, studentId : String, studentName : String)

}