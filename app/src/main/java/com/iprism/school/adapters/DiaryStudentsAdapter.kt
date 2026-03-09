package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.adapters.AttandanceStudentsAdapter.StudentsAttendanceViewHolder
import com.iprism.school.databinding.AttendanceItemBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.interfaces.OnDiaryStudentsClickListener
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class DiaryStudentsAdapter(var context: Context, var students: MutableList<Student?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: OnDiaryStudentsClickListener

    fun setupListener(listener: OnDiaryStudentsClickListener) {
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (students[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryStudentViewHolder {

        return (if (viewType == VIEW_TYPE_ITEM) {
            val binding = AttendanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            DiaryStudentViewHolder(binding)
        } else {
            val itemLoadingBinding = ItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ItemLoadingViewHolder(itemLoadingBinding)
        }) as DiaryStudentViewHolder
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is DiaryStudentViewHolder) {
            holder.binding.apply {
                val student = students[position]
                textView49.text =
                    "${student!!.first_name} ${student.middle_name} ${student.last_name}"

                if (student!!.child_image.isNotEmpty()) {
                    Glide.with(context)
                        .load(Constants.IMAGES_URL + student.child_image)
                        .error(R.drawable.cartoon_img)
                        .into(imageView17)
                } else {
                    imageView17.setImageResource(R.drawable.cartoon_img)
                }

                imageView21.setImageResource(
                    if (student.isSelected)
                        R.drawable.attendance_selected_img
                    else
                        R.drawable.attendance_un_select_img
                )
                root.setOnClickListener {
                    notifyDataSetChanged()
                    listener.onItemClick(student.id)
                }
            }
        }

    }

    fun selectAll(select: Boolean) {
        students.forEach { it!!.isSelected = select }
        notifyDataSetChanged()
    }

    class DiaryStudentViewHolder(var binding: AttendanceItemBinding) :
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
