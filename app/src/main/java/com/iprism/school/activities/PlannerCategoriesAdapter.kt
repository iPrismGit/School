package com.iprism.school.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.PlannerCategoryItemBinding
import com.iprism.school.interfaces.OnPlannerClickListener
import com.iprism.school.model.plannersandresources.Category

class PlannerCategoriesAdapter(private var context: Context, private var categories : List<Category>) : RecyclerView.Adapter<PlannerCategoriesAdapter.PlannerCategoryViewHolder>() {

    private lateinit var listener: OnPlannerClickListener

    fun setupListener(listener: OnPlannerClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlannerCategoriesAdapter.PlannerCategoryViewHolder {
        var binding = PlannerCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlannerCategoriesAdapter.PlannerCategoryViewHolder,
        position: Int
    ) {
        var category = categories[position]
         var id = ""
        if (category.id.length == 1){
            id = "0" + category.id
        }else{
            id = category.id
        }
        holder.binding.idTxt.text = id
        holder.binding.nameTxt.text = category.name
        holder.binding.root.setOnClickListener { view ->
            listener.onCategoryClick(category.id)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class PlannerCategoryViewHolder(var binding: PlannerCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

}