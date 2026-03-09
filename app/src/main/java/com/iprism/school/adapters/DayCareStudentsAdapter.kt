package com.iprism.school.adapters

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.databinding.StudentAttandanceItemBinding
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.daycare.Student
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class DayCareStudentsAdapter(var students: ArrayList<Student?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: OnDayCareClickListener

    fun setupListener(listener: OnDayCareClickListener) {
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (students[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val binding = StudentAttandanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return (if (viewType == VIEW_TYPE_ITEM) {
            DayCareStudentViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        })

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val student = students[position]
        if (holder is DayCareStudentViewHolder){
            val context = holder.binding.root.context
            holder.binding.apply {
                holder.binding.attendanceCb.visibility = View.GONE
                holder.binding.stuName.text =
                    student!!.first_name + " " + student.middle_name + " " + student.last_name
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
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }


    class DayCareStudentViewHolder(var binding: StudentAttandanceItemBinding) :
        RecyclerView.ViewHolder(binding.root)

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

}