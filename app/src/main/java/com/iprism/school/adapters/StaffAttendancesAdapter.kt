package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.databinding.StaffAttendanceItemBinding

class StaffAttendancesAdapter(context: Context) :
    Adapter<StaffAttendancesAdapter.StaffAttendanceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StaffAttendancesAdapter.StaffAttendanceViewHolder {
        var binding: StaffAttendanceItemBinding =
            StaffAttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffAttendancesAdapter.StaffAttendanceViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class StaffAttendanceViewHolder(binding: StaffAttendanceItemBinding) :
        ViewHolder(binding.root) {

    }

}