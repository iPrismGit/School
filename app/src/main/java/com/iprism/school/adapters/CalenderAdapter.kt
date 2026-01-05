package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.CalenderItemBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.eventsmodel.Event

class CalenderAdapter(var context : Context, private val studentList: List<Event>) : RecyclerView.Adapter<CalenderAdapter.ViewHolders>() {

    class ViewHolders(var binding: CalenderItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private lateinit var listener: OnCalenderClickListener

    fun setupListener(listener: OnCalenderClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: CalenderItemBinding = CalenderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val student = studentList[position]
        val event = studentList[position]

        holder.binding.nameTxt.text  = event.title
        holder.binding.startDateTxt.text  =  "Start Date : " + student.start_date
        holder.binding.endDateTxt.text  = "End Date : " +  student.end_date
        holder.binding.timeTxt.text  = student.hour + " : " + student.minute + " am"
        holder.binding.detailsTxt.text  = "Details : " + student.description

        holder.binding.imageViewIv.setOnClickListener { view ->
            listener.onItemClick(student.id, student.title, student.image)
        }

    }
    override fun getItemCount(): Int = studentList.size

}