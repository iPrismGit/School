package com.iprism.school.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R

import com.iprism.school.databinding.ItemSelectStudentBinding
import com.iprism.school.model.studentsmodel.Student


class StudentMessageSelectAdapter(private val students: ArrayList<Student?>) :
    RecyclerView.Adapter<StudentMessageSelectAdapter.StudentMessageSelectViewHolder>() {

    private var selectedPosition = -1
    private var isAllSelected = false


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentMessageSelectAdapter.StudentMessageSelectViewHolder {
        var binding =
            ItemSelectStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentMessageSelectViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentMessageSelectAdapter.StudentMessageSelectViewHolder,
        position: Int
    ) {
        var student = students[position]
        if (student != null) {
            holder.binding.nameTxt.text =
                student.first_name + " " + student.middle_name + " " + student.last_name
        }
        if (selectedPosition == position) {
            holder.binding.imgCheck.visibility = View.VISIBLE
            holder.binding.nameTxt.setTextColor(
                ContextCompat.getColor(
                    holder.binding.root.context,
                    R.color.blue1
                )
            )
        } else {
            holder.binding.nameTxt.setTextColor(
                ContextCompat.getColor(
                    holder.binding.root.context,
                    R.color.black
                )
            )
            holder.binding.imgCheck.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int = students.size

    class StudentMessageSelectViewHolder(var binding: ItemSelectStudentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

