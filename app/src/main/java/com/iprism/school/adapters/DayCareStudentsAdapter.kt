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
import com.iprism.school.databinding.CareStudentItemBinding
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.databinding.StudentAttandanceItemBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.Response.GroupStudents
import com.iprism.school.model.Response.StudentList
import com.iprism.school.model.daycare.Student
import com.iprism.school.utils.Constants

class DayCareStudentsAdapter(var context: Context, var students: List<Student>) :
    RecyclerView.Adapter<DayCareStudentsAdapter.DayCareStudentViewHolder>() {

    private lateinit var listener: OnDayCareClickListener

    fun setupListener(listener: OnDayCareClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayCareStudentsAdapter.DayCareStudentViewHolder {
        var binding =
            StudentAttandanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayCareStudentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DayCareStudentsAdapter.DayCareStudentViewHolder,
        position: Int
    ) {
        var student = students[position]
        holder.binding.attendanceCb.visibility = View.GONE
        holder.binding.stuName.text =
            student.first_name + " " + student.middle_name + " " + student.last_name
        if (student.child_image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + student.child_image).error(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.cartoon_img
                )
            ).into(holder.binding.profileIv)
        } else {
            holder.binding.profileIv.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.cartoon_img
                )
            )
        }

        holder.binding.root.setOnClickListener { view ->
            listener.onItemLick(student.id)
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }

    class DayCareStudentViewHolder(var binding: StudentAttandanceItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}