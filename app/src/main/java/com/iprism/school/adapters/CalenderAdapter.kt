package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.databinding.CalenderItemBinding
import com.iprism.school.databinding.ConsentItemBinding
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.Response.CalenderDetailListnn
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.Constants

class CalenderAdapter(
    var activity: Context,
    private val studentList: List<CalenderDetailListnn>,
    var OnItemCallPic: ((CalenderDetailListnn)  ->Unit )? = null
) :
    RecyclerView.Adapter<CalenderAdapter.ViewHolders>() {

    class ViewHolders(var binding: CalenderItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: CalenderItemBinding = CalenderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val student = studentList[position]

        holder.binding.nameTxt.text  = student.subject
        holder.binding.dateTxt.text  = student.date
        holder.binding.dayTxt.text  = student.day
        holder.binding.timeTv.text  = student.time

//        Glide.with(activity)
//            .load(Constants.IMAGES_URL+student.attachment)
//            .into(holder.binding.imageView)
//
//        holder.binding.nameTv.text = student.student_name+"( "+student.admission_id+" )"
//        holder.binding.recivetv.text = response[position].payment_type.toString()
//        holder.binding.type.text = response[position].fee_type.toString()


        holder.itemView.setOnClickListener {
            OnItemCallPic!!.invoke(studentList[position])
        }
//


    }
    override fun getItemCount(): Int = studentList.size
//    override fun getItemCount(): Int = 7

}