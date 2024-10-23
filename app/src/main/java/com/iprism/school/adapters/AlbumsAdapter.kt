package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityAlbumsBinding
import com.iprism.school.databinding.AlbumItemBinding
import com.iprism.school.interfaces.OnAlbumClickListener

class AlbumsAdapter(context: Context) : Adapter<AlbumsAdapter.AlbumViewHolder>() {

    private lateinit var listener: OnAlbumClickListener

    fun setListener(listener: OnAlbumClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumsAdapter.AlbumViewHolder {
        var binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumsAdapter.AlbumViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 5
    }

    class AlbumViewHolder(var binding: AlbumItemBinding): ViewHolder(binding.root){

    }
}