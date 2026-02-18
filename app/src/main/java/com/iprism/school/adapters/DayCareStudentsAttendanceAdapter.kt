package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.AttendanceItemBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.interfaces.OnDayCareStudentClickListener
import com.iprism.school.model.SelectedStudent
import com.iprism.school.model.daycare.Student
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class DayCareStudentsAttendanceAdapter(
    private val students: ArrayList<Student?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: OnDayCareStudentClickListener? = null

    private val selectedStudents = ArrayList<SelectedStudent>()

    fun setupListener(listener: OnDayCareStudentClickListener) {
        this.listener = listener
    }

    fun initializePresentStudents() {
        selectedStudents.clear()
        students.filterNotNull().forEach {
            if (it.attendance_status.equals("present", true)) {
                selectedStudents.add(SelectedStudent(it.id))
            }
        }
        notifyDataSetChanged()
    }

    inner class DayCareStudentAttendanceViewHolder(val binding: AttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (students[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            AttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding =
            ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return if (viewType == VIEW_TYPE_ITEM) {
            DayCareStudentAttendanceViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is DayCareStudentAttendanceViewHolder) {

            val student = students[position] ?: return
            val context = holder.itemView.context

            holder.binding.apply {

                textView49.text =
                    "${student.first_name} ${student.middle_name} ${student.last_name}"

                if (student.child_image.isNotEmpty()) {
                    Glide.with(context)
                        .load(Constants.IMAGES_URL + student.child_image)
                        .error(R.drawable.cartoon_img)
                        .into(imageView17)
                } else {
                    imageView17.setImageResource(R.drawable.cartoon_img)
                }

                if (selectedStudents.any { it.id == student.id }) {
                    imageView21.setImageResource(R.drawable.present_img)
                } else {
                    imageView21.setImageResource(R.drawable.attendance_un_select_img)
                }

                root.setOnClickListener {

                    if (selectedStudents.any { it.id == student.id }) {
                        selectedStudents.removeAll { it.id == student.id }
                    } else {
                        selectedStudents.add(SelectedStudent(student.id))
                    }

                    notifyItemChanged(holder.adapterPosition)

                    val type =
                        if (selectedStudents.size == students.filterNotNull().size)
                            "all"
                        else
                            "single"

                    listener?.onSelectionChanged(selectedStudents, type)
                }
            }
        }
    }

    override fun getItemCount(): Int = students.size

    fun selectAll() {
        selectedStudents.clear()
        students.filterNotNull().forEach {
            selectedStudents.add(SelectedStudent(it.id))
        }
        notifyDataSetChanged()
        listener?.onSelectionChanged(selectedStudents, "all")
    }

    fun clearAll() {
        selectedStudents.clear()
        notifyDataSetChanged()
        listener?.onSelectionChanged(selectedStudents, "single")
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

}
