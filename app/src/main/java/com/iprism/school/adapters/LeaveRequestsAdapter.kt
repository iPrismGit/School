package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R
import com.iprism.school.databinding.LeaveItemBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.applyforleavemodel.LeaveRequest

class LeaveRequestsAdapter(var leaveRequests: List<LeaveRequest>) : RecyclerView.Adapter<LeaveRequestsAdapter.LeaveRequestViewHolder>() {

    private lateinit var listener: OnCalenderClickListener

    fun setupListener(listener: OnCalenderClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaveRequestsAdapter.LeaveRequestViewHolder {
        var binding = LeaveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaveRequestViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LeaveRequestsAdapter.LeaveRequestViewHolder,
        position: Int
    ) {
        var leaveRequest = leaveRequests[position]
        holder.binding.detailsTxt.text = "Details : " + leaveRequest.reason
        holder.binding.dateTxt.text = "Date : " +  leaveRequest.from_date + " - " + leaveRequest.to_date
        if (leaveRequest.status.isEmpty()){
            holder.binding.statusTxt.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.black))
            holder.binding.statusTxt.text = "Status : Request Pending"
        } else if (leaveRequest.status.equals("accepted", true)){
            holder.binding.statusTxt.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.thick_green))
            holder.binding.statusTxt.text = "Status : Request Approved"
        } else if (leaveRequest.status.equals("rejected", true)){
            holder.binding.statusTxt.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.red))
            holder.binding.statusTxt.text = "Status : Request Rejected"
        }

        holder.binding.imageViewIv.setOnClickListener { view ->
            listener.onItemClick("", "", leaveRequest.image)
        }
    }

    override fun getItemCount(): Int {
        return leaveRequests.size
    }

    class LeaveRequestViewHolder(var binding: LeaveItemBinding) : RecyclerView.ViewHolder(binding.root)
}