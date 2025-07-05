package com.iprism.school.adapters

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.ItemVideoBinding

class VideoAdapter (
    private var videoUris: MutableList<Uri>,
    private val onDeleteClick: (Uri) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoUri = videoUris[position]

        // Generate a thumbnail from the video
        holder.binding.videoThumbnail.setImageBitmap(getVideoThumbnail(holder.itemView.context, videoUri))

        // Handle delete button click
        holder.binding.deleteBtn.setOnClickListener {
            onDeleteClick(videoUri)
        }
    }

    override fun getItemCount(): Int = videoUris.size

    // Function to delete a specific video
    fun deleteVideo(uri: Uri) {
        val index = videoUris.indexOf(uri)
        if (index != -1) {
            videoUris.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    // Function to update video list
    fun updateVideos(newVideos: List<Uri>) {
        videoUris.clear()
        videoUris.addAll(newVideos)
        notifyDataSetChanged()
    }

    // Function to get video thumbnail
    private fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            val bitmap = retriever.frameAtTime
            retriever.release()
            bitmap
        } catch (e: Exception) {
            null
        }
    }
}