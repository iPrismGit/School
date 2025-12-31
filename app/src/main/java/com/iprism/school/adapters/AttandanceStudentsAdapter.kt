package com.iprism.school.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.StudentAttandanceItemBinding
import com.iprism.school.model.classteachermodel.Student
import com.iprism.school.utils.Constants
import kotlin.collections.map

class AttandanceStudentsAdapter(private val context: Context, private val studentList: List<Student>) : RecyclerView.Adapter<AttandanceStudentsAdapter.StudentsAttendanceViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttandanceStudentsAdapter.StudentsAttendanceViewHolder {
        var binding = StudentAttandanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentsAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentsAttendanceViewHolder,
        position: Int
    ) {
        val student = studentList[position]
        val binding = holder.binding

        binding.stuName.text = "${student.first_name} ${student.middle_name} ${student.last_name}"

        if (!student.child_image.isNullOrEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + student.child_image).error(ContextCompat.getDrawable(context, R.drawable.cartoon_img)).into(binding.imageView)
        } else {
            binding.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cartoon_img))
        }

        val checkBox = holder.binding.attendanceCb

        checkBox.setOnCheckedChangeListener(null)

        val isPresent = student.attendance_status == "present"

        checkBox.isChecked = isPresent
        checkBox.isEnabled = !isPresent

        binding.root.setOnClickListener {

            if (isPresent) {
                Log.d("Attendance", "Already Present, click ignored")
                return@setOnClickListener
            }
            val newState = !checkBox.isChecked
            checkBox.isChecked = newState
            student.attendance_status = if (newState) "present" else "absent"

           // listener.onStudentSelected(student, newState)
        }
    }


    override fun getItemCount(): Int {
        return studentList.size
    }

    class StudentsAttendanceViewHolder(var binding: StudentAttandanceItemBinding) : RecyclerView.ViewHolder(binding.root)

}