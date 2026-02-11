package com.iprism.school.adapters
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R

import com.iprism.school.databinding.ItemSelectStudentBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.studentsmodel.Student


class StudentMessageSelectAdapter(private val students: List<Student?>) : RecyclerView.Adapter<StudentMessageSelectAdapter.StudentMessageSelectViewHolder>() {

    private lateinit var listener : OnMessageClickListener

    fun setupListener(listener: OnMessageClickListener){
        this.listener = listener
    }

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
        @SuppressLint("RecyclerView") position: Int
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

        holder.binding.root.setOnClickListener { view ->
            selectedPosition = position
            notifyDataSetChanged()
            listener.onStudentSelectClick("single", student!!.id, student.first_name + " " + student.middle_name + " " + student.last_name)
        }

    }


    override fun getItemCount(): Int = students.size

    class StudentMessageSelectViewHolder(var binding: ItemSelectStudentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

