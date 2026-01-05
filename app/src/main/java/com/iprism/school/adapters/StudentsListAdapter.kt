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
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.model.Request.StaffLsit
import com.iprism.school.model.Response.StudentListt
import com.iprism.school.utils.Constants


class StudentsListAdapter(var activity: Context, var response: List<StudentListt>, var OnItemCallBack: ((StudentListt)  ->Unit )? = null, var OnItemCallEdit: ((StudentListt)  ->Unit )? = null) : RecyclerView.Adapter<StudentsListAdapter.ViewHolders>() {

        var postionstaus = 1

    class ViewHolders(var binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: StudentItemBinding = StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

//        if (response[position].status == "1"){
//            holder.binding.deactivateBtn.text = "Deactivate"
//        }else{
//            holder.binding.deactivateBtn.text = "Activate"
//        }

        holder.binding.nameTv.text = response[position].student_name.toString()
        holder.binding.mobileTv.text = response[position].father_mobile.toString()
        holder.binding.classTv.text = response[position].class_id.toString()

        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].student_image)
            .into(holder.binding.proImg)

        holder.itemView.setOnClickListener{
            OnItemCallEdit!!.invoke(response[position])
        }

        holder.itemView.setOnClickListener{
            OnItemCallBack!!.invoke(response[position])
        }

        val mobileNumber = response[position].father_mobile.toString()
        val firstChar =   mobileNumber[0].toString()

        holder.binding.callImg.setOnClickListener{
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