package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.databinding.DairyItemBinding
import com.iprism.school.model.Response.StudentList
import com.iprism.school.utils.Constants

class DairiesAdapter(
    var activity: Context,
    var response: List<StudentList>,
    var OnItemBtn: ((StudentList)  ->Unit )? = null,
    var OnItemCallPic: ((StudentList)  ->Unit )? = null)
    : RecyclerView.Adapter<DairiesAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: DairyItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: DairyItemBinding = DairyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
                Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].student_image)
            .into(holder.binding.imageView)

        if (response[position].remarks == ""){
            holder.binding.checkBox.visibility = View.VISIBLE
            holder.binding.fillCheckBox.visibility = View.GONE
        }else{
            holder.binding.checkBox.visibility = View.GONE
            holder.binding.fillCheckBox.visibility = View.VISIBLE
        }

        holder.binding.nameTv.text = response[position].student_name+"( "+response[position].admission_id+" )"
//        holder.binding.recivetv.text = response[position].payment_type.toString()
//        holder.binding.type.text = response[position].fee_type.toString()


        holder.binding.camPic.setOnClickListener {
            OnItemCallPic!!.invoke(response[position])
        }


        holder.binding.checkBox.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }

        holder.binding.fillCheckBox.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }

    }

    override fun getItemCount(): Int {
        return response.size
    }
}