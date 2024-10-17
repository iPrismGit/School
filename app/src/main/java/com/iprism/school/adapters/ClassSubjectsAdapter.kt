package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityCreateClassSubjectsBinding
import com.iprism.school.databinding.ActivityEditRightsBinding
import com.iprism.school.databinding.ClassSubjectItemBinding
import com.iprism.school.interfaces.OnSubjectClickListener

class ClassSubjectsAdapter(context: Context):Adapter<ClassSubjectsAdapter.ClassSubjectViewHolder>() {


    private lateinit var listener: OnSubjectClickListener

    fun setListener(listener: OnSubjectClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassSubjectsAdapter.ClassSubjectViewHolder {
        var binding = ClassSubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassSubjectViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClassSubjectsAdapter.ClassSubjectViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ClassSubjectViewHolder(var binding: ClassSubjectItemBinding) : ViewHolder(binding.root){

    }

}