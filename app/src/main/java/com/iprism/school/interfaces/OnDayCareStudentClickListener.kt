package com.iprism.school.interfaces

import com.iprism.school.model.daycare.SelectedStudent

interface OnDayCareStudentClickListener {

    fun onSelectionChanged(selectedIds: ArrayList<SelectedStudent>, type: String)

}