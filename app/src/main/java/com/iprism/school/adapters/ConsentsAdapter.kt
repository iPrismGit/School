package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.ConsentItemBinding

class ConsentsAdapter(var context: Context) : RecyclerView.Adapter<ConsentsAdapter.ActiveConsentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsentsAdapter.ActiveConsentsViewHolder {
        var binding : ConsentItemBinding = ConsentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveConsentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsentsAdapter.ActiveConsentsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }


    class ActiveConsentsViewHolder(var binding: ConsentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}