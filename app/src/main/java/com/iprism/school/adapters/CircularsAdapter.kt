package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.CircularItemBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.circularmodels.Circular
import com.iprism.school.viewholders.ItemLoadingViewHolder

class CircularsAdapter(private val circulars: ArrayList<Circular?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   private lateinit var listener: OnCalenderClickListener

    fun setupListener(listener: OnCalenderClickListener){
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (circulars[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CircularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            ViewHolders(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolders) {
            val circular = circulars[position]
            holder.binding.nameTxt.text = circular!!.title
            holder.binding.dateTxt.text = "Date : " + circular.created_on
            if (circular.description != null && circular.description.isNotEmpty()) {
                holder.binding.detailsTxt.text = "Notes : " + circular.description
            } else {
                holder.binding.detailsTxt.text = "Notes : " + "Not Given"
            }

            holder.binding.imageViewIv.setOnClickListener { view ->
                listener.onItemClick(circular.id, circular.title, circular.image)
            }
        }
    }

    override fun getItemCount(): Int = circulars.size

    fun showLoadingFooter() {
        circulars.add(null)
        notifyItemInserted(circulars.size - 1)
    }

    fun removeLoadingFooter() {
        val index = circulars.indexOf(null)
        if (index != -1) {
            circulars.removeAt(index)
            notifyItemRemoved(index)
        }
    }


    class ViewHolders(var binding: CircularItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}