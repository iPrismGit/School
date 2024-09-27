package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.DayCareItemBinding
import com.iprism.school.databinding.SetIconItemBinding
import com.iprism.school.interfaces.OnDayCareClickListener

class ActivityIconsAdapter(var context: Context) : Adapter<ActivityIconsAdapter.ActivityIconsViewHolder>() {

    private lateinit var listener: OnDayCareClickListener

    fun setupListener(listener: OnDayCareClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityIconsAdapter.ActivityIconsViewHolder {
        var binding = DayCareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityIconsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActivityIconsAdapter.ActivityIconsViewHolder,
        position: Int) {
        holder.binding.dayCareNameTxt.text = "Snacks Time- $position"
        holder.binding.root.setOnClickListener(View.OnClickListener {

        })
    }

    override fun getItemCount(): Int {
        return 10
    }

    public class ActivityIconsViewHolder(var binding: DayCareItemBinding):ViewHolder(binding.root){

    }

}