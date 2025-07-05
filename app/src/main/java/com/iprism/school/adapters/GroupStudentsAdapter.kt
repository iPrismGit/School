package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.databinding.GroupStudentItemBinding
import com.iprism.school.model.Response.StudentsDetailList
import com.iprism.school.utils.Constants

class GroupStudentsAdapter(
    var activity: Context,
    var response: List<StudentsDetailList>,
    var OnItemCallBack: ((StudentsDetailList)  ->Unit )? = null,
    var OnItemCallEdit: ((StudentsDetailList)  ->Unit )? = null)
    : RecyclerView.Adapter<GroupStudentsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: GroupStudentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: GroupStudentItemBinding = GroupStudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.binding.nameTv.text = response[position].student_name.toString()
        holder.binding.admissionTv.text = response[position].admission_id.toString()

        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].student_image)
            .into(holder.binding.proPic)
    }

    override fun getItemCount(): Int {
        return response.size
    }
}