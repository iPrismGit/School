package com.iprism.school.interfaces

import com.iprism.school.model.classteachermodel.AttendanceStudent

interface OnAttendanceClickListener {

        fun onAttendanceChanged(
            selectedIds: List<String>,
            isAllSelected: Boolean
        )

}