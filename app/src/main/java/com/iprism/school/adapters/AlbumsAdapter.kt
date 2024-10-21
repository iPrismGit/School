package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityAlbumsBinding
import com.iprism.school.databinding.AlbumItemBinding

class AlbumsAdapter(context: Context) : Adapter<AlbumsAdapter.AlbumViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumsAdapter.AlbumViewHolder {
        var binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumsAdapter.AlbumViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class AlbumViewHolder(var binding: AlbumItemBinding): ViewHolder(binding.root){

    }
}