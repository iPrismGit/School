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
import com.iprism.school.databinding.SetIconItemBinding
import com.iprism.school.databinding.StudentReportItemBinding
import com.iprism.school.interfaces.OnActivityClickListener
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.Response.ActivityList
import com.iprism.school.model.Response.GroupStuuu
import com.iprism.school.utils.Constants

class SetActivityIconsAdapter(
    var activity: Context,
    var response: List<ActivityList>,
    var OnItemBtn: ((ActivityList)  ->Unit )? = null,
    var OnItemCallPic: ((ActivityList)  ->Unit )? = null
)
    : RecyclerView.Adapter<SetActivityIconsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: SetIconItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: SetIconItemBinding = SetIconItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].activity_icon)
            .placeholder(R.drawable.activity_defualt_icon)
            .into(holder.binding.iconIv)

        holder.binding.activityNameTxt.text = response[position].activity_name.toString()

        holder.itemView.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}