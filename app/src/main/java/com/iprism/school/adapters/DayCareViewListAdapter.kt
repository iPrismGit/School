package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.DayCareItemBinding
import com.iprism.school.model.Response.Daycare

import com.iprism.school.utils.Constants

class DayCareViewListAdapter(
    var activity: Context,
    var response: List<Daycare>,
    var OnItemBtn: ((Daycare)  ->Unit )? = null,
    var OnItemCallPic: ((Daycare)  ->Unit )? = null
)
    : RecyclerView.Adapter<DayCareViewListAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: DayCareItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: DayCareItemBinding = DayCareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {


        Glide.with(activity)
            .load(Constants.IMAGES_URL+response[position].image)
            .placeholder(R.drawable.activity_defualt_icon)
            .into(holder.binding.dayCareImg)

        holder.binding.dayCareNameTxt.text = response[position].name.toString()

//        if (response[position].remarks == ""){
//            holder.binding.checkBox.visibility = View.VISIBLE
//            holder.binding.fillCheckBox.visibility = View.GONE
//        }else{
//            holder.binding.checkBox.visibility = View.GONE
//            holder.binding.fillCheckBox.visibility = View.VISIBLE
//        }
//
//        holder.binding.nameTv.text = response[position].student_name+"( "+response[position].admission_id+" )"
//        holder.binding.recivetv.text = response[position].payment_type.toString()
//        holder.binding.type.text = response[position].fee_type.toString()


        holder.itemView.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }

    }

    override fun getItemCount(): Int {
        return response.size
    }
}