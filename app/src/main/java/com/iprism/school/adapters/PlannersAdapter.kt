package com.iprism.school.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.PlannerItemBinding
import com.iprism.school.model.plannersandresources.Planner
import com.iprism.school.model.plannersandresources.PlannerInner

class PlannersAdapter(var context: Context, var planners: List<PlannerInner>) :
    RecyclerView.Adapter<PlannersAdapter.PlannerViewHolder>(),
    PlannersInnerAdapter.OnPlannerInnerClickListener {

    private lateinit var listener: OnPlannerOuterClickListener


    fun setupListener(listener: OnPlannerOuterClickListener) {
        this.listener = listener
    }

    interface OnPlannerOuterClickListener {
        fun onItemClick(
            id: String,
            catId: String,
            subject: String,
            description: String,
            category: String,
            subCategory: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlannersAdapter.PlannerViewHolder {
        val binding = PlannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlannersAdapter.PlannerViewHolder, position: Int) {
        val planner = planners[position]
        holder.binding.apply {
            subjectTxt.text = "Subject : ${planner.subject}"
            descriptionTxt.text = "Description : ${planner.description}"
            dateTxt.text = "Date : ${planner.created_on}"
        }
    }

    override fun getItemCount(): Int {
        return planners.size
    }

    override fun onItemClick(
        id: String,
        catId: String,
        subject: String,
        description: String,
        category: String,
        subCategory: String
    ) {
        listener.onItemClick(id, catId, subject, description, category, subCategory)
    }

    class PlannerViewHolder(var binding: PlannerItemBinding) : RecyclerView.ViewHolder(binding.root)
}