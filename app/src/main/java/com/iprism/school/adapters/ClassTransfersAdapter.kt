package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityClassTransferBinding
import com.iprism.school.databinding.ClassStudentItemBinding

class ClassTransfersAdapter(context: Context) : Adapter<ClassTransfersAdapter.ClassTransferViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassTransfersAdapter.ClassTransferViewHolder {
        var binding = ClassStudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassTransferViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClassTransfersAdapter.ClassTransferViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ClassTransferViewHolder(var binding: ClassStudentItemBinding): ViewHolder(binding.root){

    }

}