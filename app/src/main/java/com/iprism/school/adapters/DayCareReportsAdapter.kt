package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.databinding.DaycareReportItemBinding

class DayCareReportsAdapter (var context: Context) : Adapter<DayCareReportsAdapter.DayCareReportViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayCareReportsAdapter.DayCareReportViewHolder {
        var binding = DaycareReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayCareReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayCareReportsAdapter.DayCareReportViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
       return 5
    }

    class DayCareReportViewHolder(binding: DaycareReportItemBinding) : ViewHolder(binding.root){

    }

}