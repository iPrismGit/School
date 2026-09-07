package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.parentapp.model.notifications.NotificationsItem
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.databinding.NotificationItemBinding
import com.iprism.school.viewholders.ItemLoadingViewHolder
import com.iprism.school.viewholders.NotificationViewHolder

class NotificationsAdapter(var notifications: ArrayList<NotificationsItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (notifications[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            NotificationViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationViewHolder) {
            val notification = notifications[position]
            holder.binding.titleTxt.text = notification!!.catName
            holder.binding.messageTxt.text = notification.message
            holder.binding.dateTxt.text = notification.createdOn ?: ""
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun showLoadingFooter() {
        notifications.add(null)
        notifyItemInserted(notifications.size - 1)
    }

    fun removeLoadingFooter() {
        val index = notifications.indexOf(null)
        if (index != -1) {
            notifications.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
