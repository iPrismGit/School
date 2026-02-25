package com.iprism.school.interfaces

import com.iprism.school.model.classteachermodel.AttendanceStudent
import com.iprism.school.model.daycare.SelectedStudent

interface OnAttendanceClickListener {

        fun onAttendanceChanged(
            selectedIds: ArrayList<AttendanceStudent>,
            type: String
        )

}