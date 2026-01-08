package com.iprism.school.interfaces

interface OnCreatedDiariesClickListener {

    fun onDeleteClickListener(dairyId : String)
    fun onInformationClickListener(studentId : String, image : String, type : String, details : String, firstName : String, middleName : String, lastName : String)

}