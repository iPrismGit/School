package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.AttendanceItemBinding

class StudentsAttendanceAdapter(var context: Context) : Adapter<StudentsAttendanceAdapter.StudentAttendanceViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentsAttendanceAdapter.StudentAttendanceViewHolder {
       var binding  = AttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentsAttendanceAdapter.StudentAttendanceViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class StudentAttendanceViewHolder(var binding: AttendanceItemBinding): ViewHolder(binding.root){

    }
}