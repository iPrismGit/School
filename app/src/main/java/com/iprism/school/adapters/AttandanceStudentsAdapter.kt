package com.iprism.school.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.StudentAttandanceItemBinding
import com.iprism.school.interfaces.OnAttendanceClickListener
import com.iprism.school.model.classteachermodel.Student
import com.iprism.school.utils.Constants

class AttandanceStudentsAdapter(
    private val context: Context,
    private val studentList: List<Student>
) : RecyclerView.Adapter<AttandanceStudentsAdapter.StudentsAttendanceViewHolder>() {

    private lateinit var listener: OnAttendanceClickListener

    fun setupListener(listener: OnAttendanceClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : StudentsAttendanceViewHolder {
        val binding = StudentAttandanceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentsAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentsAttendanceViewHolder, position: Int) {
        val student = studentList[position]
        val binding = holder.binding

        bindStudentInfo(binding, student)

        updateCheckIcon(binding, student.isSelected)

        binding.root.setOnClickListener {
            student.isSelected = !student.isSelected
            updateCheckIcon(binding, student.isSelected)

            notifySelectionChanged()
        }
    }

    override fun getItemCount(): Int = studentList.size

    private fun updateCheckIcon(
        binding: StudentAttandanceItemBinding,
        selected: Boolean
    ) {
        binding.attendanceCb.setImageResource(
            if (selected)
                R.drawable.attendance_selected_img
            else
                R.drawable.attendance_un_select_img
        )
    }

    private fun notifySelectionChanged() {
        val selectedIds = studentList
            .filter { it.isSelected }
            .map { it.id }

        val isAllSelected =
            studentList.isNotEmpty() && selectedIds.size == studentList.size

        listener.onAttendanceChanged(selectedIds, isAllSelected)
    }

    fun selectAll(select: Boolean) {
        studentList.forEach {
            it.isSelected = select
        }
        notifyDataSetChanged()
        notifySelectionChanged()
    }

    class StudentsAttendanceViewHolder(
        val binding: StudentAttandanceItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private fun bindStudentInfo(
        binding: StudentAttandanceItemBinding,
        student: Student
    ) {

        student.isSelected = student.attendance_status.equals("present", true)

        updateCheckIcon(binding, student.isSelected)

        binding.stuName.text =
            "${student.first_name} ${student.middle_name} ${student.last_name}"

        if (!student.child_image.isNullOrEmpty()) {
            Glide.with(context)
                .load(Constants.IMAGES_URL + student.child_image)
                .error(ContextCompat.getDrawable(context, R.drawable.cartoon_img))
                .into(binding.profileIv)
        } else {
            binding.profileIv.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.cartoon_img)
            )
        }
    }

}


