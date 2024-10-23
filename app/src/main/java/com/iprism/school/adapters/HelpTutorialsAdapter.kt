package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityHelpTutorialsBinding
import com.iprism.school.databinding.TutorialItemBinding

class HelpTutorialsAdapter(context: Context) : Adapter<HelpTutorialsAdapter.HelpTutorialViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HelpTutorialsAdapter.HelpTutorialViewHolder {
        var binding = TutorialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelpTutorialViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HelpTutorialsAdapter.HelpTutorialViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class HelpTutorialViewHolder(var binding: TutorialItemBinding):ViewHolder(binding.root){

    }

}