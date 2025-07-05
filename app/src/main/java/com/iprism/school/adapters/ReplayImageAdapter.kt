package com.iprism.school.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R

class ReplayImageAdapter (private val images: MutableList<Uri>,
                          private val onDeleteClick: (Uri) -> Unit
) : RecyclerView.Adapter<ReplayImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imgviewww)
        val btnDelete: ImageView = view.findViewById(R.id.delete_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reply_image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = images[position]

        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(imageUri)
            .into(holder.imageView)

        // Handle delete button click
        holder.btnDelete.setOnClickListener {
            onDeleteClick(imageUri)
        }
    }

    override fun getItemCount(): Int = images.size

    fun deleteImage(uri: Uri) {
        images.remove(uri)
        notifyDataSetChanged()
    }
}