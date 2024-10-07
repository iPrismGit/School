package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.SetIconItemBinding
import com.iprism.school.interfaces.OnActivityClickListener
import com.iprism.school.interfaces.OnDayCareClickListener

class SetActivityIconsAdapter(var context: Context) :
    Adapter<SetActivityIconsAdapter.SetActivityIconViewHolder>() {

    private lateinit var listener: OnActivityClickListener

    fun setupListener(listener: OnActivityClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetActivityIconsAdapter.SetActivityIconViewHolder {
        var binding = SetIconItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SetActivityIconViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SetActivityIconsAdapter.SetActivityIconViewHolder,
        position: Int) {
        holder.binding.cameraLo.setOnClickListener( View.OnClickListener {
            listener.onItemClick("position", "position")
        })
    }

    override fun getItemCount(): Int {
       return 5
    }

    class SetActivityIconViewHolder(var binding: SetIconItemBinding) : ViewHolder(binding.root) {

    }

}