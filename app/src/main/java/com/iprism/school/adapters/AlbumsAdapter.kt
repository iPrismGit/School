package com.iprism.school.adapters

import android.content.Context
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
import com.iprism.school.model.Response.AlbumDetail
import com.iprism.school.model.Response.GroupStuuu
import com.iprism.school.utils.Constants

class AlbumsAdapter(var activity: Context,
                    var response: List<AlbumDetail>,
                    var OnItemBtn: ((AlbumDetail)  ->Unit )? = null,
                    var OnItemCallPic: ((AlbumDetail)  ->Unit )? = null
)
    : RecyclerView.Adapter<AlbumsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: AlbumItemBinding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        if (response[position].album_type == "image"){
            holder.binding.imageView37.visibility = View.GONE
        }else{
            holder.binding.imageView37.visibility = View.VISIBLE
        }

        holder.binding.closeImg.visibility = View.GONE

        if (response[position].album_content.isEmpty()){
            Glide.with(activity)
                .load(R.drawable.app_logo)
                .placeholder(R.drawable.baseline_image)
                .into(holder.binding.imageView36)
        }else {
            Glide.with(activity)
                .load(Constants.IMAGES_URL+response[position].album_content[0].file_name)
                .placeholder(R.drawable.baseline_image)
                .into(holder.binding.imageView36)
        }

        holder.itemView.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}