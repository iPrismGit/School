package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityPendingRequestsBinding
import com.iprism.school.databinding.PendingRequestItemBinding
import com.iprism.school.interfaces.PendingRequestClickListener

class PendingRequestsAdapter(context: Context) :
    Adapter<PendingRequestsAdapter.PendingRequestViewHolder>() {

    private lateinit var listener: PendingRequestClickListener

    fun setupListener(listener: PendingRequestClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingRequestsAdapter.PendingRequestViewHolder {
        var binding =
            PendingRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingRequestViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PendingRequestsAdapter.PendingRequestViewHolder,
        position: Int
    ) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onPendingItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 3
    }

    class PendingRequestViewHolder(var binding: PendingRequestItemBinding) :
        ViewHolder(binding.root) {

    }
}