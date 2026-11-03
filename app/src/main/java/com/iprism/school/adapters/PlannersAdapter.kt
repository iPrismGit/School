package com.iprism.school.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.adapters.StudentsAdapter.StudentViewHolder
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.databinding.PlannerItemBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.model.plannersandresources.Planner
import com.iprism.school.model.plannersandresources.PlannerInner
import com.iprism.school.viewholders.ItemLoadingViewHolder

class PlannersAdapter(var context: Context, var planners: ArrayList<PlannerInner?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: OnPlannerOuterClickListener


    fun setupListener(listener: OnPlannerOuterClickListener) {
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (planners[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PlannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            PlannerViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlannerViewHolder) {
            val planner = planners[position]
            holder.binding.apply {
                subjectTxt.text = "Subject : ${planner!!.subject}"
                descriptionTxt.text = "Description : ${planner.description}"
                dateTxt.text = "Date : ${planner.created_on}"
                root.setOnClickListener { view ->
                    listener.onItemClick(planner.id, "", planner.subject, planner.description, "", "")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return planners.size
    }

    fun showLoadingFooter() {
        planners.add(null)
        notifyItemInserted(planners.size - 1)
    }

    fun removeLoadingFooter() {
        val index = planners.indexOf(null)
        if (index != -1) {
            planners.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class PlannerViewHolder(var binding: PlannerItemBinding) : RecyclerView.ViewHolder(binding.root)
}