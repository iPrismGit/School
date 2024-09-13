package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.ConsentItemBinding

class ActiveConsentsAdapter(var activity: FragmentActivity) : RecyclerView.Adapter<ActiveConsentsAdapter.ActiveConsentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveConsentsAdapter.ActiveConsentsViewHolder {
        var binding : ConsentItemBinding = ConsentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveConsentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActiveConsentsAdapter.ActiveConsentsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }


    class ActiveConsentsViewHolder(var binding: ConsentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}