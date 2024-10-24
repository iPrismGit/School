package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityMessageDetailsBinding
import com.iprism.school.databinding.MessageItemBinding
import com.iprism.school.interfaces.OnMessageClickListener

class MessagesAdapter(context: Context) : Adapter<MessagesAdapter.MessageViewHolder>() {

    private lateinit var listener: OnMessageClickListener

    fun setupListener(listener: OnMessageClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessagesAdapter.MessageViewHolder {
        var binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessagesAdapter.MessageViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
       return 6
    }

    class MessageViewHolder(var binding: MessageItemBinding) : ViewHolder(binding.root){

    }

}