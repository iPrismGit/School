package com.iprism.school.adapters



import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.databinding.StaffItemBinding
import com.iprism.school.model.Request.StaffLsit
import com.iprism.school.utils.Constants


class StaffListAdapter(
    var activity: Context,
    var response: List<StaffLsit>,
    var OnItemCallBack: ((StaffLsit)  ->Unit )? = null,
    var OnItemCallEdit: ((StaffLsit)  ->Unit )? = null,
    var deactiveCallBack: ((StaffLsit)  ->Unit )? = null)
    : RecyclerView.Adapter<StaffListAdapter.ViewHolders>() {

        var postionstaus = 1

    class ViewHolders(var binding: StaffItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: StaffItemBinding = StaffItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        if (response[position].status == "1"){
            holder.binding.deactivateBtn.text = "Deactivate"
        }else{
            holder.binding.deactivateBtn.text = "Activate"
        }

        holder.binding. nameTv.text = response[position].employee_name.toString()
        holder.binding.mobileTv.text = response[position].employee_mobile.toString()
        holder.binding.departmentTv.text = response[position].employee_department.toString()

        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].employee_image)
            .into(holder.binding.imgPic)

        holder.itemView.setOnClickListener{
            OnItemCallEdit!!.invoke(response[position])
        }

        holder.binding.deactivateBtn.setOnClickListener{
            deactiveCallBack!!.invoke(response[position])
        }

        val mobileNumber = response[position].employee_mobile.toString()
        val firstChar =   mobileNumber[0].toString()

        holder.binding.callIv.setOnClickListener{
            if (mobileNumber == ""||mobileNumber == null ||mobileNumber == "0"||mobileNumber.length < 10){
                Toast.makeText(activity, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
            }else if (firstChar == ""||firstChar == null||firstChar == "1"||firstChar == "2"||firstChar == "3"||firstChar == "4"||firstChar == "5"||firstChar == "0"){
                Toast.makeText(activity,"Invalid Mobile Number", Toast.LENGTH_SHORT).show()
            }else{
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:$mobileNumber")
                activity.startActivity(dialIntent)
            }
        }

    }

    override fun getItemCount(): Int {
     return response.size
    }
}