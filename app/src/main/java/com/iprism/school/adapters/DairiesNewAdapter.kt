package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.Constants

class DairiesNewAdapter(var activity: Context,
                            private val studentList: List<StudentList>,
                        var OnItemBtn: ((StudentList)  ->Unit )? = null,
                        var OnItemCallPic: ((StudentList)  ->Unit )? = null) :
    RecyclerView.Adapter<DairiesNewAdapter.ViewHolders>() {

    class ViewHolders(var binding: DairyItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: DairyItemBinding = DairyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val student = studentList[position]

        holder.binding.nameTv.text  = student.student_name.toString()+"( "+student.admission_id+" )"
        holder.binding.remarkTv.text  = student.remarks.toString()

        Glide.with(activity)
            .load(Constants.IMAGES_URL+student.student_image)
            .into(holder.binding.imageView)

        if (student.remarks == ""){
            holder.binding.checkBox.visibility = View.VISIBLE
            holder.binding.fillCheckBox.visibility = View.GONE
        }else{
            holder.binding.checkBox.visibility = View.GONE
            holder.binding.fillCheckBox.visibility = View.VISIBLE
        }

        holder.binding.nameTv.text = student.student_name+"( "+student.admission_id+" )"
//        holder.binding.recivetv.text = response[position].payment_type.toString()
//        holder.binding.type.text = response[position].fee_type.toString()


        holder.binding.camPic.setOnClickListener {
            OnItemCallPic!!.invoke(studentList[position])
        }


        holder.binding.checkBox.setOnClickListener {
            OnItemBtn!!.invoke(studentList[position])
        }

        holder.binding.fillCheckBox.setOnClickListener {
            OnItemBtn!!.invoke(studentList[position])
        }


    }
    override fun getItemCount(): Int = studentList.size
}