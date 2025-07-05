package com.iprism.school.activities.circular

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R

class AudioAdapter(
    private val audioList: MutableList<AudioItem>
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    // Callback for deletion
    var onDeleteClick: ((position: Int) -> Unit)? = null

    // Holds the MediaPlayer for the currently playing item (if any)
    private var currentMediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition: Int = -1
    private var currentIsPaused: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val audioItem = audioList[position]
        holder.tvRecordingName.text = "Recording ${position + 1}"

        // Update CheckBox state and listener
        holder.cbSelect.isChecked = audioItem.isSelected
        holder.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            audioItem.isSelected = isChecked
        }

        // Update Play/Pause button text
        if (position == currentPlayingPosition && currentMediaPlayer?.isPlaying == true) {
            holder.btnPlayPause.text = "Pause"
        } else {
            holder.btnPlayPause.text = "Play"
        }

        holder.btnPlayPause.setOnClickListener {
            // If the same item is clicked, toggle pause/resume
            // If the same item is clicked, stop playback instead of pausing.
            if (position == currentPlayingPosition) {
                currentMediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        player.stop()
                        player.release()
                        currentMediaPlayer = null
                        currentPlayingPosition = -1
                        holder.btnPlayPause.text = "Play"
                        // Notify the changed item to refresh UI
                        notifyItemChanged(position)
                    }
                }
            } else {
                // If a different item is clicked, stop any current playback and play this file
                currentMediaPlayer?.let {
                    it.stop()
                    it.release()
                }
                currentMediaPlayer = MediaPlayer().apply {
                    setDataSource(audioItem.path)
                    prepare()
                    start()
                }
                // Notify previous playing item to update its button
                notifyItemChanged(currentPlayingPosition)
                currentPlayingPosition = position
                holder.btnPlayPause.text = "Stop"
            }
        }

        // Set up delete button click listener
        holder.btnDelete.setOnClickListener {
            onDeleteClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int = audioList.size

    fun releasePlayer() {
        currentMediaPlayer?.release()
        currentMediaPlayer = null
        currentPlayingPosition = -1
    }

    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbSelect: CheckBox = itemView.findViewById(R.id.cbSelect)
        val tvRecordingName: TextView = itemView.findViewById(R.id.tvRecordingName)
        val btnPlayPause: Button = itemView.findViewById(R.id.btnPlayPause)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}