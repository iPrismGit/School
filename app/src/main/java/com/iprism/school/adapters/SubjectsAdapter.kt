package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivitySubjectsBinding
import com.iprism.school.databinding.SubjectItemBinding
import com.iprism.school.interfaces.OnSubjectClickListener

class SubjectsAdapter(context: Context) : Adapter<SubjectsAdapter.SubjectViewHolder>() {

    private lateinit var listener: OnSubjectClickListener

    fun setupListener(listener: OnSubjectClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectsAdapter.SubjectViewHolder {
        var binding = SubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectsAdapter.SubjectViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 5
    }

    class SubjectViewHolder(var binding: SubjectItemBinding) : ViewHolder(binding.root) {

    }

}