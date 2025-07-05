package com.iprism.school.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ActivityAlbumsBinding
import com.iprism.school.databinding.AlbumItemBinding
import com.iprism.school.databinding.StudentReportItemBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.model.Response.AlbumContentList
import com.iprism.school.model.Response.AlbumDetail
import com.iprism.school.model.Response.GroupStuuu
import com.iprism.school.utils.Constants

class AlbumsImageAdapter(var activity: Context,
                         var response: List<AlbumContentList>,
                         val attachment_type: String = "",
                         var OnItemBtn: ((AlbumContentList)  ->Unit )? = null)
    : Adapter<AlbumsImageAdapter.ViewHolders>() {
    var postionstaus = 1
    class ViewHolders(var binding: AlbumItemBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: AlbumItemBinding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        Log.d("album_type",attachment_type.toString())

        if (attachment_type == "image"){
            holder.binding.imageView37.visibility = View.GONE
        }else{
            holder.binding.imageView37.visibility = View.VISIBLE
        }

//        if (response[position].album_content.isEmpty()){
//            Glide.with(activity)
//                .load(R.drawable.app_logo)
//                .placeholder(R.drawable.activity_defualt_icon)
//                .into(holder.binding.imageView36)
//        }else {

            Glide.with(activity)
                .load(Constants.IMAGES_URL+response[position].file_name)
                .placeholder(R.drawable.baseline_image)
                .into(holder.binding.imageView36)
//        }


//        holder.binding.studentNameTv.text = "Title : "+ response[position].title.toString()

//        if (response[position].has_daycare_report == "Yes"){
//            holder.binding.greenImg.visibility = View.VISIBLE
//            holder.binding.redImg.visibility = View.GONE
//            holder.binding.reportTv.text = "Daycare report created"
//        }else{
//            holder.binding.greenImg.visibility = View.GONE
//            holder.binding.redImg.visibility = View.VISIBLE
//
//            holder.binding.reportTv.text = "No daycare report"
//        }

        holder.binding.closeImg.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}