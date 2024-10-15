package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityGroupDetailsBinding
import com.iprism.school.databinding.GroupStudentItemBinding

class GroupStudentsAdapter(var context: Context) : Adapter<GroupStudentsAdapter.GroupStudentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupStudentsAdapter.GroupStudentViewHolder {
        var binding = GroupStudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupStudentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GroupStudentsAdapter.GroupStudentViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return  10
    }

    class GroupStudentViewHolder(var binding: GroupStudentItemBinding) : ViewHolder(binding.root){

    }

}