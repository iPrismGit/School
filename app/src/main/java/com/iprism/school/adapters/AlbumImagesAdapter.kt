package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.AlbumInnerItemBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.model.albums.AlbumsGallery
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class AlbumImagesAdapter(var context: Context, var albumImagesList: ArrayList<AlbumsGallery?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: OnAlbumClickListener

    fun setupListener(listener: OnAlbumClickListener) {
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (albumImagesList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  RecyclerView.ViewHolder {
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        var binding =
            AlbumInnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            AlbumImageViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is AlbumImageViewHolder) {
            val album = albumImagesList[position]
            val context = holder.itemView.context
            if (!album!!.image.isNullOrEmpty()) {
                Glide.with(context).load(Constants.IMAGES_URL + album.image)
                    .error(ContextCompat.getDrawable(context, R.drawable.dummy_logo))
                    .into(holder.binding.albumImg)
            } else {
                holder.binding.albumImg.setImageResource(R.drawable.dummy_logo)
            }
            holder.binding.root.setOnClickListener { view ->
                listener.onCoverClick(album.id.toString(), album.image)
            }
        }
    }

    override fun getItemCount(): Int {
        return albumImagesList.size
    }

    class AlbumImageViewHolder(var binding: AlbumInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showLoadingFooter() {
        albumImagesList.add(null)
        notifyItemInserted(albumImagesList.size - 1)
    }

    fun removeLoadingFooter() {
        val index = albumImagesList.indexOf(null)
        if (index != -1) {
            albumImagesList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}