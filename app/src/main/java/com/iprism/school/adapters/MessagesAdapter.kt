package com.iprism.school.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ActivityMessageDetailsBinding
import com.iprism.school.databinding.AlbumItemBinding
import com.iprism.school.databinding.MessageItemBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.Response.AlbumDetail
import com.iprism.school.model.Response.InboxMessageList
import com.iprism.school.utils.Constants

class MessagesAdapter(var activity: Context,
                      var response: List<InboxMessageList>,
                      var OnItemBtn: ((InboxMessageList)  ->Unit )? = null,
                      var starBtn: ((InboxMessageList)  ->Unit )? = null,
                      var OnItemCallPic: ((InboxMessageList)  ->Unit )? = null)
    : RecyclerView.Adapter<MessagesAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: MessageItemBinding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

            Glide.with(activity)
                .load(Constants.IMAGES_URL+response[position].image)
                .placeholder(R.drawable.baseline_image)
                .into(holder.binding.imgg)

        if (response[position].read_message == ""){

            holder.binding.nameTv.text = response[position].name.toString()
            holder.binding.subjectTv.text = response[position].subject.toString()
            holder.binding.msgTv.text = response[position].message.toString()
            holder.binding.dateTv.text = response[position].date.toString()

            holder.binding.nameTv.setTypeface(null, android.graphics.Typeface.BOLD)
            holder.binding.subjectTv.setTypeface(null, android.graphics.Typeface.BOLD)
            holder.binding.msgTv.setTypeface(null, android.graphics.Typeface.BOLD)
            holder.binding.dateTv.setTypeface(null, android.graphics.Typeface.BOLD)
        }else{

            holder.binding.nameTv.text = response[position].name.toString()
            holder.binding.subjectTv.text = response[position].subject.toString()
            holder.binding.msgTv.text = response[position].message.toString()
            holder.binding.dateTv.text = response[position].date.toString()

            holder.binding.nameTv.setTypeface(null, android.graphics.Typeface.NORMAL)
            holder.binding.subjectTv.setTypeface(null, android.graphics.Typeface.NORMAL)
            holder.binding.msgTv.setTypeface(null, android.graphics.Typeface.NORMAL)
            holder.binding.dateTv.setTypeface(null, android.graphics.Typeface.NORMAL)
        }

        if (response[position].starred_message == ""){
            holder.binding.starImg.setColorFilter(Color.GRAY) // Gray Tint for Read Messages
        }else{
            holder.binding.starImg.setColorFilter(
                ContextCompat.getColor(holder.itemView.context, R.color.attendance_not_marked)
            )
        }


        holder.itemView.setOnClickListener {
            OnItemBtn!!.invoke(response[position])
        }

        holder.binding.starImg.setOnClickListener {
            starBtn!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}