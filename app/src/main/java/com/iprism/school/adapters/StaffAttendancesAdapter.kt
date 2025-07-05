package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.databinding.ClassSubjectItemBinding
import com.iprism.school.databinding.StaffAttendanceItemBinding
import com.iprism.school.model.Response.AttendanceLsittt
import com.iprism.school.model.Response.SubjectTcharList

class StaffAttendancesAdapter(
    var activity: Context,
    var response: List<AttendanceLsittt>,
    var OnItemCallBack: ((AttendanceLsittt)  ->Unit )? = null,
    var OnItemCallEdit: ((AttendanceLsittt)  ->Unit )? = null)
    : Adapter<StaffAttendancesAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: StaffAttendanceItemBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: StaffAttendanceItemBinding = StaffAttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.binding.staffNameTxt.text = response[position].employee_name.toString()
        holder.binding.staffRoleTxt.text = response[position].employee_designation.toString()
        holder.binding.inTimeTxt.text = response[position].in_time.toString()
        holder.binding.outTimeTxt.text = response[position].out_time.toString()

//        Glide.with(activity)
//            .load(Constants.IMAGES_URL+response[position].attachment)
//            .into(holder.binding.priPic)

//        holder.itemView.setOnClickListener{
//            OnItemCallEdit!!.invoke(response[position])
//        }

        holder.itemView.setOnClickListener{
            OnItemCallBack!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}