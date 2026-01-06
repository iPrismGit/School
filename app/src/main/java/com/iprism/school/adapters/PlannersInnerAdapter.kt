package com.iprism.school.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.PlannerInnerItemBinding
import com.iprism.school.model.plannersandresources.PlannerInner

class PlannersInnerAdapter(var context: Context, var innerPlanners : List<PlannerInner>) : RecyclerView.Adapter<PlannersInnerAdapter.PlannerInnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlannersInnerAdapter.PlannerInnerViewHolder {
        var binding = PlannerInnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerInnerViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: PlannersInnerAdapter.PlannerInnerViewHolder,
        position: Int
    ) {
       var innerPlanner = innerPlanners[position]
        holder.binding.plannerNameTxt.text = innerPlanner.subject
        holder.binding.categoryTxt.text = "Category : " + innerPlanner.category
        holder.binding.subCategoryTxt.text = "Sub Category : " + innerPlanner.sub_category
        holder.binding.dateTxt.text = "Date : " + innerPlanner.created_date
        var id = ""
        if (innerPlanner.cat_id.length == 1){
            id = "0" + innerPlanner.cat_id
        }else{
            id = innerPlanner.cat_id
        }
        holder.binding.idTxt.text = id
    }

    override fun getItemCount(): Int {
        return innerPlanners.size
    }

    class PlannerInnerViewHolder(var binding: PlannerInnerItemBinding) : RecyclerView.ViewHolder(binding.root)
}