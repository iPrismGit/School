package com.iprism.school.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.databinding.ItemSelectStudentBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.daycare.Student
import com.iprism.school.viewholders.ItemLoadingViewHolder

class DayCareMessageStudentsAdapter(var students: List<Student?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener : OnMessageClickListener

    fun setupListener(listener: OnMessageClickListener){
        this.listener = listener
    }

    private var selectedPosition = -1
    private var isAllSelected = false

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (students[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    inner class DayCareMessageStudentViewHolder(var binding: ItemSelectStudentBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemSelectStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding =
            ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == DayCareMessageStudentsAdapter.Companion.VIEW_TYPE_ITEM) {
            DayCareMessageStudentViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        if (holder is DayCareMessageStudentViewHolder){
            holder.binding.apply {
                var student = students[position]
                if (student != null) {
                    nameTxt.text =
                        student.first_name + " " + student.middle_name + " " + student.last_name
                }
                if (isAllSelected) {
                    imgCheck.visibility = View.VISIBLE
                    nameTxt.setTextColor(
                        ContextCompat.getColor(root.context, R.color.blue1)
                    )
                } else if (selectedPosition == position) {
                    imgCheck.visibility = View.VISIBLE
                    nameTxt.setTextColor(
                        ContextCompat.getColor(root.context, R.color.blue1)
                    )
                } else {
                    imgCheck.visibility = View.GONE
                    nameTxt.setTextColor(
                        ContextCompat.getColor(root.context, R.color.black)
                    )
                }

                root.setOnClickListener {

                    if (isAllSelected) {
                        isAllSelected = false
                    }

                    selectedPosition = position
                    notifyDataSetChanged()

                    listener.onStudentSelectClick(
                        "single",
                        student!!.id.toString(),
                        student.first_name + " " + student.middle_name + " " + student.last_name
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
      return students.size
    }

    fun selectAllStudents() {
        isAllSelected = true
        selectedPosition = -1
        notifyDataSetChanged()

        listener.onStudentSelectClick(
            "broadcast",
            "0",
            "Group Message"
        )
    }

}