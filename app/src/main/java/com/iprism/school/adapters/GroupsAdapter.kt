package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityGroupsBinding
import com.iprism.school.databinding.GroupItemBinding
import com.iprism.school.interfaces.OnGroupItemClickListener

class GroupsAdapter(var  context: Context) : Adapter<GroupsAdapter.GroupViewHolder> (){

    private lateinit var listener: OnGroupItemClickListener

    fun setListener(listener: OnGroupItemClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.GroupViewHolder {
        var binding = GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.GroupViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick("")
        })
    }

    override fun getItemCount(): Int {
        return 5
    }
    class GroupViewHolder(var binding: GroupItemBinding): ViewHolder(binding.root){

    }
}