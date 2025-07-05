package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.databinding.ActivitySubjectsBinding
import com.iprism.school.databinding.GroupItemBinding
import com.iprism.school.databinding.SubjectItemBinding
import com.iprism.school.interfaces.OnSubjectClickListener
import com.iprism.school.model.Response.GroupList
import com.iprism.school.model.Response.SubjectLsit
import com.iprism.school.utils.Constants

class SubjectsAdapter(
    var activity: Context,
    var response: List<SubjectLsit>,
    var OnItemCallBack: ((SubjectLsit)  ->Unit )? = null,
    var OnItemCallEdit: ((SubjectLsit)  ->Unit )? = null)
    : RecyclerView.Adapter<SubjectsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: SubjectItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: SubjectItemBinding = SubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.binding.subNameTv.text = response[position].subject_name.toString()

//        Glide.with(activity)
//            .load(Constants.IMAGES_URL+response[position].attachment)
//            .into(holder.binding.priPic)

//        holder.itemView.setOnClickListener{
//            OnItemCallEdit!!.invoke(response[position])
//        }

        holder.itemView.setOnClickListener{
            OnItemCallBack!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}