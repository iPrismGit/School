package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.FragmentStaffActiveBinding
import com.iprism.school.databinding.StaffItemBinding

class StaffAdapter(context: Context) : Adapter<StaffAdapter.StaffViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StaffAdapter.StaffViewHolder {
        var binding = StaffItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffAdapter.StaffViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class StaffViewHolder(var binding: StaffItemBinding) : ViewHolder(binding.root){

    }

}