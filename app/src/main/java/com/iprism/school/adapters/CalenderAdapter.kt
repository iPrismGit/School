package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.CalenderItemBinding
import com.iprism.school.databinding.ConsentItemBinding

class CalenderAdapter (var context: Context) : Adapter<CalenderAdapter.CalenderViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalenderAdapter.CalenderViewHolder {
        var binding = CalenderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalenderAdapter.CalenderViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 8
    }

    class CalenderViewHolder(var binding: CalenderItemBinding) : ViewHolder(binding.root){

    }

}