package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.AlbumInnerItemBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.model.albums.AlbumsGallery
import com.iprism.school.utils.Constants

class AlbumImagesAdapter(var context: Context, var albumImagesList: List<AlbumsGallery>) :
    RecyclerView.Adapter<AlbumImagesAdapter.AlbumImageViewHolder>() {

    private lateinit var listener: OnAlbumClickListener

    fun setupListener(listener: OnAlbumClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumImagesAdapter.AlbumImageViewHolder {
        var binding =
            AlbumInnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumImageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AlbumImagesAdapter.AlbumImageViewHolder,
        position: Int
    ) {
        var album = albumImagesList[position]
        if (album.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + album.image)
                .error(ContextCompat.getDrawable(context, R.drawable.dummy_logo))
                .into(holder.binding.albumImg)
        } else {
            holder.binding.albumImg.setImageResource(R.drawable.dummy_logo)
        }
        holder.binding.root.setOnClickListener { view ->
            listener.onCoverClick(album.id, album.image)
        }
    }

    override fun getItemCount(): Int {
        return albumImagesList.size
    }

    class AlbumImageViewHolder(var binding: AlbumInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}