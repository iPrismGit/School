package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.DayCareItemBinding
import com.iprism.school.databinding.SetIconItemBinding
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.Response.ActivityIconsList
import com.iprism.school.model.Response.ActivityList
import com.iprism.school.utils.Constants

class ActivityIconsAdapter(
    var activity: Context,
    var response: List<ActivityIconsList>,
    var OnItemBtn: ((ActivityIconsList)  ->Unit )? = null,
    var OnItemCallPic: ((ActivityIconsList)  ->Unit )? = null
)
    : RecyclerView.Adapter<ActivityIconsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: DayCareItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: DayCareItemBinding = DayCareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].activity_icon)
            .placeholder(R.drawable.activity_defualt_icon)
            .into(holder.binding.dayCareImg)

        holder.binding.dayCareNameTxt.text = response[position].icon_name.toString()

        holder.itemView.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}