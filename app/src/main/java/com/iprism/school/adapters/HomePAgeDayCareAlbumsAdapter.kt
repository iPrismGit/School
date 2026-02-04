package com.iprism.school.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.HomeAlbumItemBinding
import com.iprism.school.model.homepagemodel.DayCareAlbumCoverHome
import com.iprism.school.utils.Constants

class HomePAgeDayCareAlbumsAdapter(private var homeDayCareAlbums : List<DayCareAlbumCoverHome>) : RecyclerView.Adapter<HomePAgeDayCareAlbumsAdapter.HomePageDayCareAlbumViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePAgeDayCareAlbumsAdapter.HomePageDayCareAlbumViewHolder {
        var binding = HomeAlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePageDayCareAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomePAgeDayCareAlbumsAdapter.HomePageDayCareAlbumViewHolder,
        position: Int
    ) {
        var albumCover = homeDayCareAlbums[position]
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
        return homeDayCareAlbums.size
    }

    class HomePageDayCareAlbumViewHolder(var binding: HomeAlbumItemBinding) : RecyclerView.ViewHolder(binding.root)
}