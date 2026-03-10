package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.AttendanceItemBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.interfaces.OnAttendanceClickListener
import com.iprism.school.model.classteachermodel.AttendanceStudent
import com.iprism.school.model.classteachermodel.Student
import com.iprism.school.model.daycare.SelectedStudent
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class AttandanceStudentsAdapter(

    private val studentList: ArrayList<Student?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var attendanceStatus: String = ""
    private val selectedStudents = mutableSetOf<Int>()

    private val modifiedStudents = mutableSetOf<Int>()
    private lateinit var listener: OnAttendanceClickListener

    fun setupListener(listener: OnAttendanceClickListener) {
        this.listener = listener
    }

    fun addPresentStudents(newStudents: List<Student?>) {
        newStudents.filterNotNull().forEach {
            if (it.attendance_status.equals("present", true)) {
                selectedStudents.add(it.id)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (studentList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return if (viewType == AttandanceStudentsAdapter.Companion.VIEW_TYPE_ITEM) {
            val binding = AttendanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            StudentsAttendanceViewHolder(binding)
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
        if (holder is StudentsAttendanceViewHolder) {

            val student = studentList[position] ?: return
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

                        listener?.onAttendanceChanged(
                            selectedStudents.map { AttendanceStudent(it) } as ArrayList<AttendanceStudent>,
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

                        listener?.onAttendanceChanged(
                            modifiedStudents.map { SelectedStudent(it) } as ArrayList<AttendanceStudent>,
                            "single"
                        )
                    }

                    notifyItemChanged(holder.adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int = studentList.size

    class StudentsAttendanceViewHolder(
        val binding: AttendanceItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    fun selectAll() {
        if (attendanceStatus.equals("attendance_not_given", true)) {

            selectedStudents.clear()
            studentList.filterNotNull().forEach {
                selectedStudents.add(it.id)
            }

            notifyDataSetChanged()

            listener?.onAttendanceChanged(
                selectedStudents.map { AttendanceStudent(it) } as ArrayList<AttendanceStudent>,
                "all"
            )
        }
    }

    fun clearAll() {
        if (attendanceStatus.equals("attendance_not_given", true)) {

            selectedStudents.clear()
            notifyDataSetChanged()

            listener?.onAttendanceChanged(
                arrayListOf(),
                "clear"
            )
        }
    }

    fun showLoadingFooter() {
        studentList.add(null)
        notifyItemInserted(studentList.size - 1)
    }

    fun removeLoadingFooter() {
        val index = studentList.indexOf(null)
        if (index != -1) {
            studentList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun resetSelection() {
        selectedStudents.clear()
        modifiedStudents.clear()
        notifyDataSetChanged()
    }

}


