package com.iprism.school.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.CareStudentItemBinding
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.databinding.StudentAttandanceItemBinding
import com.iprism.school.model.Response.AttendanceStudents
import com.iprism.school.model.Response.GroupStudents
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.Constants
import kotlin.collections.map

class AttandanceStudentsAdapter(
    private val context: Context,
    private val studentList: List<AttendanceStudents>,
    private val onSelectionChanged: (List<String>, List<String>, Int, Int) -> Unit, // Callback for counts
    private var selectAll: Boolean
) : RecyclerView.Adapter<AttandanceStudentsAdapter.ViewHolder>() {

    private val selectedIds = mutableSetOf<String>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentName: TextView = view.findViewById(R.id.stu_name)
        val checkBox: CheckBox = view.findViewById(R.id.stu_checkBox)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(student: AttendanceStudents) {

            Glide.with(context)
            .load(Constants.IMAGES_URL+student.student_image)
            .into(imageView)

            studentName.text = student.student_name
            checkBox.isChecked = selectedIds.contains(student.student_id)

            checkBox.setOnCheckedChangeListener(null) // Prevent unwanted triggers during binding
            checkBox.isChecked = selectedIds.contains(student.student_id)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedIds.add(student.student_id)
                } else {
                    selectedIds.remove(student.student_id)
                }
                updateSelection()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_attandance_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studentList[position])
    }

    override fun getItemCount(): Int = studentList.size

    fun toggleSelectAll(selectAll: Boolean) {
        if (selectAll) {
            selectedIds.clear()
            selectedIds.addAll(studentList.map { it.student_id }) // Select all
        } else {
            selectedIds.clear() // Deselect all
        }
        notifyDataSetChanged()
        updateSelection()
    }

    private fun updateSelection() {
        val selectedList = selectedIds.toList()
        val unselectedList = studentList.map { it.student_id }.filter { it !in selectedIds }
        onSelectionChanged(selectedList, unselectedList, selectedList.size, unselectedList.size)
    }
}