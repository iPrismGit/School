package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.CircularItemBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.circularmodels.Circular

class CircularsAdapter(var activity: Context, private val circulars: List<Circular>) : RecyclerView.Adapter<CircularsAdapter.ViewHolders>() {

   private lateinit var listener: OnCalenderClickListener

    fun setupListener(listener: OnCalenderClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding = CircularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val circular = circulars[position]
        holder.binding.nameTxt.text  = circular.title
        holder.binding.dateTxt.text  = "Date : " + circular.created_on
        if(circular.description != null && circular.description.isNotEmpty()){
            holder.binding.detailsTxt.text  = "Notes : " + circular.description
        } else{
            holder.binding.detailsTxt.text  = "Notes : " + "Not Given"
        }

        holder.binding.imageViewIv.setOnClickListener { view ->
            listener.onItemClick(circular.id, circular.title, circular.image)
        }

    }

    override fun getItemCount(): Int = circulars.size


    class ViewHolders(var binding: CircularItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}