package com.iprism.school.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.CareStudentItemBinding
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.model.Response.GroupStudents
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.Constants

class DayCareStudentsAdapter(
    private val groups: List<GroupStudents>,
    private val onSelectedIdsChanged: (List<String>) -> Unit,
    private var selectAll: Boolean // To handle select all state

) : RecyclerView.Adapter<DayCareStudentsAdapter.GroupViewHolder>() {

    private val selectedIdsList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = CareStudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    // This method returns all IDs of the groups in the list
    fun getAllIds(): List<String> {
        return groups.map { it.id }
    }

    // Update the "Select All" state
    fun updateSelectAllState(selectAll: Boolean) {
        this.selectAll = selectAll
        selectedIdsList.clear()  // Clear previously selected IDs
        if (selectAll) {
            // Add all group IDs to the list if "Select All" is checked
            groups.forEach { selectedIdsList.add(it.id) }
        }
        onSelectedIdsChanged(selectedIdsList)
        notifyDataSetChanged() // Notify the adapter to refresh the UI
    }

    inner class GroupViewHolder(private val binding: CareStudentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(group: GroupStudents) {

            Glide.with(binding.root)
                .load(Constants.IMAGES_URL+groups[position].student_image)
                .into(binding.imageView)

            binding.nameTv.text = group.student_name
            binding.checkBox.isChecked = selectedIdsList.contains(group.id)

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!selectedIdsList.contains(group.id)) {
                        selectedIdsList.add(group.id)
                        Log.d("selectedIdsList", "Added: ${group.id}")
                    }
                } else {
                    selectedIdsList.remove(group.id)
                    Log.d("selectedIdsList", "Removed: ${group.id}")
                }

                // Notify the parent activity with the updated list
                onSelectedIdsChanged(selectedIdsList)
            }
        }
    }}