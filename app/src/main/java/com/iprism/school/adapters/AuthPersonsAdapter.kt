package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.CalenderItemBinding
import com.iprism.school.databinding.ConsentItemBinding
import com.iprism.school.interfaces.OnConsentClickListener
import com.iprism.school.model.Response.ConsentListDetail
import com.iprism.school.utils.Constants

class AuthPersonsAdapter(
    var activity: Context,
    private val studentList: List<ConsentListDetail>,
    var OnItemCallPic: ((ConsentListDetail)  ->Unit )? = null) :
    RecyclerView.Adapter<AuthPersonsAdapter.ViewHolders>() {

    class ViewHolders(var binding: ConsentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: ConsentItemBinding = ConsentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val student = studentList[position]

        holder.binding.nameTxt.text  = student.class_names.toString()
        holder.binding.dateTxt.text  = student.created_date.toString()
        holder.binding.descriptionTxt.text  = student.details.toString()

        Glide.with(activity)
            .load(R.drawable.message_profile)
            .into(holder.binding.profileImg)

//        holder.binding.nameTv.text = student.student_name+"( "+student.admission_id+" )"
//        holder.binding.recivetv.text = response[position].payment_type.toString()
//        holder.binding.type.text = response[position].fee_type.toString()


        holder.itemView.setOnClickListener {
            OnItemCallPic!!.invoke(studentList[position])
        }
    }
    override fun getItemCount(): Int = studentList.size

}