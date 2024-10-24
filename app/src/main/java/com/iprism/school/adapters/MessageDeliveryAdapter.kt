package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityMessageInfoBinding
import com.iprism.school.databinding.MessageDeliveryItemBinding

class MessageDeliveryAdapter (context: Context) : Adapter<MessageDeliveryAdapter.MessageDeliveryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageDeliveryAdapter.MessageDeliveryViewHolder {
        var binding = MessageDeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageDeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MessageDeliveryAdapter.MessageDeliveryViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int {
        return 10
    }

    class MessageDeliveryViewHolder(var binding: MessageDeliveryItemBinding) : ViewHolder(binding.root){

    }

}