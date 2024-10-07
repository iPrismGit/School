package com.iprism.school.model

data class ClassInfo(val name: String, val classId: Int) {

    override fun toString(): String {
        return name
    }

}