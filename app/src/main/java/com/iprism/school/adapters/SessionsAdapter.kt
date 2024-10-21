package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.SessionItemBinding
import com.iprism.school.interfaces.OnSessionClickListener

class SessionsAdapter(context: Context):Adapter<SessionsAdapter.SessionViewHolder>() {

    private lateinit var listener: OnSessionClickListener

    fun setListener(listener: OnSessionClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionsAdapter.SessionViewHolder {
        var binding = SessionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionsAdapter.SessionViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 6
    }

    class SessionViewHolder(var binding: SessionItemBinding) : ViewHolder(binding.root){

    }

}