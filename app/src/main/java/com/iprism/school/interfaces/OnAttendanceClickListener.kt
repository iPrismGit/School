package com.iprism.school.interfaces

interface OnAttendanceClickListener {

        fun onAttendanceChanged(
            selectedIds: List<String>,
            isAllSelected: Boolean
        )

}