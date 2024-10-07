package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.DairyItemBinding

class DairiesAdapter (var context: Context) : Adapter<DairiesAdapter.DairyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DairiesAdapter.DairyViewHolder {
        var binding = DairyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DairyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DairiesAdapter.DairyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }

    class DairyViewHolder(binding: DairyItemBinding) : ViewHolder(binding.root){

    }
}