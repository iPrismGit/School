package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup

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

    var attendanceStatus: String = ""

    private var listener: OnDayCareStudentClickListener? = null

    private val selectedStudents = mutableSetOf<Int>()

    private val modifiedStudents = mutableSetOf<Int>()

    fun setupListener(listener: OnDayCareStudentClickListener) {
        this.listener = listener
    }
    fun initializePresentStudents() {
        selectedStudents.clear()

        students.filterNotNull().forEach {
            if (it.attendance_status.equals("present", true)) {
                selectedStudents.add(it.id)
            }
        }

        modifiedStudents.clear()
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

        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = AttendanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            DayCareStudentAttendanceViewHolder(binding)
        } else {
            val itemLoadingBinding = ItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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

                val isSelected = selectedStudents.contains(student.id)

                if (isSelected) {
                    imageView21.setImageResource(R.drawable.present_img)
                } else {
                    imageView21.setImageResource(R.drawable.attendance_un_select_img)
                }

                root.setOnClickListener {

                    if (attendanceStatus.equals("attendance_not_given", true)) {

                        if (selectedStudents.contains(student.id)) {
                            selectedStudents.remove(student.id)
                        } else {
                            selectedStudents.add(student.id)
                        }

                        listener?.onSelectionChanged(
                            selectedStudents.map { SelectedStudent(it) } as ArrayList<SelectedStudent>,
                            "single"
                        )
                    }

                    else if (attendanceStatus.equals("attendance_given", true)) {

                        val originalPresent =
                            student.attendance_status.equals("present", true)

                        val currentlySelected = selectedStudents.contains(student.id)

                        if (currentlySelected) {
                            selectedStudents.remove(student.id)
                        } else {
                            selectedStudents.add(student.id)
                        }

                        val nowSelected = selectedStudents.contains(student.id)

                        if (originalPresent == nowSelected) {
                            modifiedStudents.remove(student.id)
                        } else {
                            modifiedStudents.add(student.id)
                        }

                        listener?.onSelectionChanged(
                            modifiedStudents.map { SelectedStudent(it) } as ArrayList<SelectedStudent>,
                            "single"
                        )
                    }

                    notifyItemChanged(holder.adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int = students.size

    // ✅ Select All only for attendance_not_given
    fun selectAll() {
        if (attendanceStatus.equals("attendance_not_given", true)) {

            selectedStudents.clear()
            students.filterNotNull().forEach {
                selectedStudents.add(it.id)
            }

            notifyDataSetChanged()

            listener?.onSelectionChanged(
                selectedStudents.map { SelectedStudent(it) } as ArrayList<SelectedStudent>,
                "all"
            )
        }
    }

    fun clearAll() {
        if (attendanceStatus.equals("attendance_not_given", true)) {

            selectedStudents.clear()
            notifyDataSetChanged()

            listener?.onSelectionChanged(
                arrayListOf(),
                "clear"
            )
        }
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

