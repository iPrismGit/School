package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R
import com.iprism.school.databinding.ItemLeaveRequestBinding
import com.iprism.school.model.leaverequestmodel.Request
import com.iprism.school.model.plannersandresources.PlannerInner

class StudentLeaveRequestsAdapter(var requests: List<Request>) :
    RecyclerView.Adapter<StudentLeaveRequestsAdapter.StudentLeaveRequestsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentLeaveRequestsAdapter.StudentLeaveRequestsViewHolder {
        var binding =
            ItemLeaveRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentLeaveRequestsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentLeaveRequestsAdapter.StudentLeaveRequestsViewHolder,
        position: Int
    ) {
        var request = requests[position]
        holder.binding.tvName.text = request.name
        holder.binding.tvDate.text = request.from_date + " - " + request.to_date
        holder.binding.tvReason.text = request.reason
        if (request.image.isNotEmpty()) {
            holder.binding.tvAttachment.visibility = View.VISIBLE
        } else {
            holder.binding.tvAttachment.visibility = View.GONE
        }
        if (request.status.isEmpty()) {
            holder.binding.tvStatus.text = "Pending"
            holder.binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    holder.binding.root.context,
                    R.color.gray1
                )
            )
            holder.binding.rejectionReason.visibility = View.GONE
        } else if (request.status.equals("rejected", true)) {
            holder.binding.tvStatus.text = "Rejected"
            holder.binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    holder.binding.root.context,
                    R.color.red
                )
            )
            holder.binding.rejectionReason.visibility = View.VISIBLE
            holder.binding.rejectionReason.text = request.reject_reason
        } else if (request.status.equals("accepted", true)) {
            holder.binding.tvStatus.text = "Approved"
            holder.binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    holder.binding.root.context,
                    R.color.green
                )
            )
            holder.binding.rejectionReason.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return requests.size
    }

    class StudentLeaveRequestsViewHolder(var binding: ItemLeaveRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

}