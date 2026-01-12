package com.iprism.school.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.DayCareItemBinding
import com.iprism.school.databinding.StudentReportItemBinding
import com.iprism.school.model.Response.Daycare
import com.iprism.school.model.Response.GroupStuuu

import com.iprism.school.utils.Constants

class DayCareReportsStudentsListAdapter(
    var activity: Context,
    var response: List<GroupStuuu>,
    var OnItemBtn: ((GroupStuuu)  ->Unit )? = null,
    var OnItemCallPic: ((GroupStuuu)  ->Unit )? = null
)
    : RecyclerView.Adapter<DayCareReportsStudentsListAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: StudentReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: StudentReportItemBinding = StudentReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].student_image)
            .placeholder(R.drawable.activity_defualt_icon)
            .into(holder.binding.studentImg)

        holder.binding.studentNameTv.text = response[position].student_name.toString()

        if (response[position].has_daycare_report == "Yes"){
            holder.binding.greenImg.visibility = View.VISIBLE
            holder.binding.redImg.visibility = View.GONE

            holder.binding.reportTv.text = "Daycare report created"

        }else{
            holder.binding.greenImg.visibility = View.GONE
            holder.binding.redImg.visibility = View.VISIBLE

            holder.binding.reportTv.text = "No daycare report"
        }

        holder.itemView.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}