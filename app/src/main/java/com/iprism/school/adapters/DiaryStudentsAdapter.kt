package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.AttendanceItemBinding
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.utils.Constants

class DiaryStudentsAdapter(
    var context: Context,
    var students: MutableList<Student>
) : RecyclerView.Adapter<DiaryStudentsAdapter.DiaryStudentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryStudentViewHolder {
        val binding = AttendanceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DiaryStudentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DiaryStudentViewHolder,
        position: Int
    ) {
        val student = students[position]

        holder.binding.textView49.text =
            "${student.first_name} ${student.middle_name} ${student.last_name}"

        // Load image
        if (student.child_image.isNotEmpty()) {
            Glide.with(context)
                .load(Constants.IMAGES_URL + student.child_image)
                .error(R.drawable.cartoon_img)
                .into(holder.binding.imageView17)
        } else {
            holder.binding.imageView17.setImageResource(R.drawable.cartoon_img)
        }

        holder.binding.imageView21.setImageResource(
            if (student.isSelected)
                R.drawable.attendance_selected_img
            else
                R.drawable.attendance_un_select_img
        )

        holder.binding.root.setOnClickListener {
            student.isSelected = !student.isSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = students.size

    fun selectAll(select: Boolean) {
        students.forEach { it.isSelected = select }
        notifyDataSetChanged()
    }

    class DiaryStudentViewHolder(var binding: AttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
