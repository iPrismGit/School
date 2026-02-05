package com.iprism.school.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.MessageItemBinding
import com.iprism.school.utils.Constants

class SentMessagesAdapter()
    : Adapter<SentMessagesAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: MessageItemBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: MessageItemBinding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {


    }

    override fun getItemCount(): Int {
        return 0
    }
}