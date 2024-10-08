package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.FragmentScannerBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.interfaces.OnCalenderClickListener

class StudentsAdapter(var context: Context) : Adapter<StudentsAdapter.StudentViewHolder>() {

    private lateinit var listener: OnCalenderClickListener
    fun setListener(listener: OnCalenderClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsAdapter.StudentViewHolder {
        var  binding = StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentsAdapter.StudentViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 10
    }

    class StudentViewHolder(var binding: StudentItemBinding) : ViewHolder(binding.root){

    }

}