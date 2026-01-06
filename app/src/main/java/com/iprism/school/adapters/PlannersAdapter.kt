package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.PlannerItemBinding
import com.iprism.school.model.plannersandresources.Planner

class PlannersAdapter(var context: Context, var planners : List<Planner>) : RecyclerView.Adapter<PlannersAdapter.PlannerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlannersAdapter.PlannerViewHolder {
        var binding = PlannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlannersAdapter.PlannerViewHolder, position: Int) {
       var planner = planners[position]
        holder.binding.plannerDateTxt.text = planner.date
        var plannersInnerAdapter = PlannersInnerAdapter(context, planner.planners)
        holder.binding.plannersInnerRv.adapter = plannersInnerAdapter
        holder.binding.plannersInnerRv.layoutManager = LinearLayoutManager(context)
    }

    override fun getItemCount(): Int {
        return planners.size
    }

    class PlannerViewHolder(var binding: PlannerItemBinding) : RecyclerView.ViewHolder(binding.root)
}