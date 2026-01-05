package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.R
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.databinding.FragmentScannerBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.interfaces.OnStudentClickListener
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.utils.Constants

class StudentsAdapter(var context: Context, var students : List<Student>) : Adapter<StudentsAdapter.StudentViewHolder>() {

    private lateinit var listener: OnStudentClickListener

    fun setupListener(listener: OnStudentClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsAdapter.StudentViewHolder {
        var  binding = StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentsAdapter.StudentViewHolder, position: Int) {
        var student = students[position]
        holder.binding.nameTv.text = student.first_name + " " + student.middle_name + " " + student.last_name
        holder.binding.classTv.text = student.class_name + " " + student.section_name
        holder.binding.mobileTv.text = student.primary_mobile
        if (student.child_image != null && student.child_image.isEmpty()){
            Glide.with(context).load(Constants.IMAGES_URL + student.child_image).error(ContextCompat.getDrawable(context, com.iprism.school.R.drawable.cartoon_img)).into(holder.binding.proImg)
        } else{
          holder.binding.proImg.setImageDrawable(ContextCompat.getDrawable(context, com.iprism.school.R.drawable.cartoon_img))
        }
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onCallClick(student.primary_mobile)
        })
    }

    override fun getItemCount(): Int {
        return students.size
    }

    class StudentViewHolder(var binding: StudentItemBinding) : ViewHolder(binding.root){

    }

}