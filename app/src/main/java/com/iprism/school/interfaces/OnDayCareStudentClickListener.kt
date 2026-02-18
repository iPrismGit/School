package com.iprism.school.interfaces

import com.iprism.school.model.SelectedStudent

interface OnDayCareStudentClickListener {

    fun onSelectionChanged(selectedIds: ArrayList<SelectedStudent>, type: String)

}