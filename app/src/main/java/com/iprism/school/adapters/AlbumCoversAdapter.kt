package com.iprism.school.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.iprism.school.R
import com.iprism.school.databinding.AlbumCoverItemBinding
import com.iprism.school.model.albums.AlbumCover
import com.iprism.school.utils.Constants

class AlbumCoversAdapter(var context: Context, var albumCovers: List<AlbumCover>) :
    RecyclerView.Adapter<AlbumCoversAdapter.AlbumCoverViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumCoversAdapter.AlbumCoverViewHolder {
        var binding = AlbumCoverItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumCoverViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AlbumCoverViewHolder,
        position: Int
    ) {
        val albumCover = albumCovers[position]

        holder.binding.titleTxt.text = albumCover.title
        holder.binding.descriptionTxt.text = albumCover.description
        holder.binding.dateTxt.text = albumCover.date
        holder.binding.imageLoader.visibility = View.VISIBLE

        if (albumCover.image.isNotEmpty()) {
            Glide.with(context)
                .load(Constants.IMAGES_URL + albumCover.image)
                .error(R.drawable.dummy_logo)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.binding.imageLoader.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.binding.imageLoader.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.binding.thumbnailImg)

        } else {
            holder.binding.imageLoader.visibility = View.GONE
            holder.binding.thumbnailImg.setImageResource(R.drawable.dummy_logo)
        }
    }


    override fun getItemCount(): Int {
        return albumCovers.size
    }

    class AlbumCoverViewHolder(var binding: AlbumCoverItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}