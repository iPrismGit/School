package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.MessageItemBinding


class MessagesAdapter() : RecyclerView.Adapter<MessagesAdapter.ViewHolders>() {

    class ViewHolders(var binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: MessageItemBinding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {


    }

    override fun getItemCount(): Int {
        return 0
    }

}