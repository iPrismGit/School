package com.iprism.school.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.databinding.ActivityGroupsBinding
import com.iprism.school.databinding.GroupItemBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.interfaces.OnGroupItemClickListener
import com.iprism.school.model.Response.GroupList
import com.iprism.school.model.Response.StudentListt
import com.iprism.school.utils.Constants

class GroupsAdapter(
    var activity: Context,
    var response: List<GroupList>,
    var OnItemCallBack: ((GroupList)  ->Unit )? = null,
    var OnItemCallEdit: ((GroupList)  ->Unit )? = null)
    : RecyclerView.Adapter<GroupsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: GroupItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: GroupItemBinding = GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.binding.groupName.text = response[position].group_name.toString()
        holder.binding.studentsCount.text = "Students: " +response[position].group_students.toString()
        holder.binding.descriptionTv.text = response[position].group_description.toString()

        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].attachment)
            .into(holder.binding.priPic)

        holder.itemView.setOnClickListener{
            OnItemCallEdit!!.invoke(response[position])
        }

        holder.itemView.setOnClickListener{
            OnItemCallBack!!.invoke(response[position])
        }

//        val mobileNumber = response[position].father_mobile.toString()
//        val firstChar =   mobileNumber[0].toString()
//
//        holder.binding.callImg.setOnClickListener{
//            if (mobileNumber == ""||mobileNumber == null ||mobileNumber == "0"||mobileNumber.length < 10){
//                Toast.makeText(activity, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
//            }else if (firstChar == ""||firstChar == null||firstChar == "1"||firstChar == "2"||firstChar == "3"||firstChar == "4"||firstChar == "5"||firstChar == "0"){
//                Toast.makeText(activity,"Invalid Mobile Number", Toast.LENGTH_SHORT).show()
//            }else{
//                val dialIntent = Intent(Intent.ACTION_DIAL)
//                dialIntent.data = Uri.parse("tel:$mobileNumber")
//                activity.startActivity(dialIntent)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}