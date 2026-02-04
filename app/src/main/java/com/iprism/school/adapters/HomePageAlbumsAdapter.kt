package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.HomeAlbumItemBinding
import com.iprism.school.model.homepagemodel.AlbumCoverHome
import com.iprism.school.utils.Constants

class HomePageAlbumsAdapter(private var homeAlbums : List<AlbumCoverHome>) : RecyclerView.Adapter<HomePageAlbumsAdapter.HomePageAlbumViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePageAlbumsAdapter.HomePageAlbumViewHolder {
       var binding = HomeAlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePageAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomePageAlbumsAdapter.HomePageAlbumViewHolder,
        position: Int
    ) {
        var albumCover = homeAlbums[position]
        holder.binding.titleTxt.text = albumCover.title
        holder.binding.descriptionTxt.text = albumCover.description
        if (albumCover.image.isNotEmpty()){
            Glide.with(holder.binding.root.context)
                .load(Constants.IMAGES_URL + albumCover.image).error(ContextCompat.getDrawable(holder.binding.root.context, R.drawable.dummy_logo)).into(holder.binding.imageView36)
        } else{
            holder.binding.imageView36.setImageResource(R.drawable.dummy_logo)
        }
    }

    override fun getItemCount(): Int {
        return homeAlbums.size
    }

    class HomePageAlbumViewHolder(var binding: HomeAlbumItemBinding) : RecyclerView.ViewHolder(binding.root)
}