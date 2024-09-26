package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityDiaryDeliveryReportsBinding
import com.iprism.school.databinding.DeliveryItemBinding

class DeliveryReportsAdapter(var context: Context): Adapter<DeliveryReportsAdapter.DeliveryReportViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeliveryReportsAdapter.DeliveryReportViewHolder {
        var binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryReportViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DeliveryReportsAdapter.DeliveryReportViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }

    class DeliveryReportViewHolder(var binding: DeliveryItemBinding): ViewHolder(binding.root){

    }

}