package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.DayCareItemBinding
import com.iprism.school.databinding.ReportsItemBinding
import com.iprism.school.databinding.StudentReportItemBinding
import com.iprism.school.model.Response.Daycare
import com.iprism.school.model.Response.GroupStuuu
import com.iprism.school.model.Response.StudentDaycareReport

import com.iprism.school.utils.Constants

class DayCareReportsDetailsListAdapter(
    var activity: Context,
    var response: List<StudentDaycareReport>,
    var OnItemBtn: ((StudentDaycareReport)  ->Unit )? = null,
    var OnItemCallPic: ((StudentDaycareReport)  ->Unit )? = null
)
    : RecyclerView.Adapter<DayCareReportsDetailsListAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: ReportsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: ReportsItemBinding = ReportsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        if (response[position].report_type == "activity"){

            holder.binding.qtyTxt.visibility = View.GONE
            holder.binding.endTimeTxt.visibility = View.VISIBLE

            holder.binding.mealNameTxt.text = "Name  : "+response[position].activity_name.toString()
            holder.binding.startTimeTxt.text = "Start Time : "+response[position].start_time.toString()
            holder.binding.endTimeTxt.text ="End Time : "+ response[position].end_time.toString()

        }else{

            holder.binding.qtyTxt.visibility = View.VISIBLE
            holder.binding.endTimeTxt.visibility = View.GONE

            holder.binding.mealNameTxt.text = "Name : "+response[position].meal_name.toString()
            holder.binding.startTimeTxt.text = "Start Time : "+response[position].start_time.toString()
            holder.binding.qtyTxt.text ="Quantity : "+ response[position].quantity.toString()
        }

        holder.binding.deleteBtn.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }

    }

    override fun getItemCount(): Int {
        return response.size
    }
}