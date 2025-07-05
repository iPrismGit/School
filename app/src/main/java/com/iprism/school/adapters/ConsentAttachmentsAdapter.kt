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
import com.iprism.school.databinding.ImageItemBinding
import com.iprism.school.model.Response.AttachmentSingleConsent
import com.iprism.school.utils.Constants

class ConsentAttachmentsAdapter(
    var activity: Context,
    private val studentList: List<AttachmentSingleConsent>,
    var OnItemCallPic: ((AttachmentSingleConsent)  ->Unit )? = null,
    var OnItemCalldelete: ((AttachmentSingleConsent)  ->Unit )? = null) :
    RecyclerView.Adapter<ConsentAttachmentsAdapter.ViewHolders>() {

    class ViewHolders(var binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: ImageItemBinding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val student = studentList[position]

//        holder.binding.nameTxt.text  = student.subject
//        holder.binding.dateTxt.text  = student.date
//        holder.binding.dayTxt.text  = student.day
//        holder.binding.timeTv.text  = student.time

        holder.binding.deleteImg.visibility = View.GONE

        Glide.with(activity)
            .load(Constants.IMAGES_URL+student.attachment)
            .into(holder.binding.imgviewww)

//        holder.binding.nameTv.text = student.student_name+"( "+student.admission_id+" )"
//        holder.binding.recivetv.text = response[position].payment_type.toString()
//        holder.binding.type.text = response[position].fee_type.toString()


        holder.binding.imgviewww.setOnClickListener {
            OnItemCallPic!!.invoke(studentList[position])
        }


     holder.binding.deleteImg.setOnClickListener {
            OnItemCalldelete!!.invoke(studentList[position])
        }



    }
    override fun getItemCount(): Int = studentList.size

}