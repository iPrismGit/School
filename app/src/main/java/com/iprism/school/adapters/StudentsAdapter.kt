package com.iprism.school.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.adapters.CircularsAdapter.ViewHolders
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.interfaces.OnStudentClickListener
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class StudentsAdapter(private var students : ArrayList<Student?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: OnStudentClickListener

    fun setupListener(listener: OnStudentClickListener){
        this.listener = listener
    }

    init {
        Log.d("listResponse", students.toString())
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (students[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            StudentViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StudentViewHolder) {
            val student = students[position]
            holder.binding.nameTv.text =
                student!!.first_name + " " + student.middle_name + " " + student.last_name
            holder.binding.classTv.text = student.class_name + " " + student.section_name
            holder.binding.mobileTv.text = student.primary_mobile
            if (student.child_image != null && student.child_image.isNotEmpty()) {
                Glide.with(holder.binding.root.context).load(Constants.IMAGES_URL + student.child_image).error(
                    ContextCompat.getDrawable(
                        holder.binding.root.context,
                        com.iprism.school.R.drawable.cartoon_img
                    )
                ).into(holder.binding.proImg)
            } else {
                holder.binding.proImg.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.binding.root.context,
                        com.iprism.school.R.drawable.cartoon_img
                    )
                )
            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                listener.onCallClick(student.primary_mobile)
            })
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }

    fun showLoadingFooter() {
        students.add(null)
        notifyItemInserted(students.size - 1)
    }

    fun removeLoadingFooter() {
        val index = students.indexOf(null)
        if (index != -1) {
            students.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class StudentViewHolder(var binding: StudentItemBinding) : ViewHolder(binding.root){

    }

}