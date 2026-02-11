package com.iprism.school.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.databinding.MessageItemBinding
import com.iprism.school.model.messagemodel.MessageThread
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder

class MessagesAdapter(private val messages: ArrayList<MessageThread?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MessageViewHolder(val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding =
            ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == MessagesAdapter.Companion.VIEW_TYPE_ITEM) {
            MessageViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is MessageViewHolder) {
            val message = messages[position]
            val context = holder.itemView.context
            holder.binding.apply {

                if (message!!.message_type.equals("single", true)){
                    nameTxt.text = message.first_name + " " + message.middle_name + " " + message.last_name

                    if (message.student_image.isNotEmpty()) {
                        Glide.with(context)
                            .load(Constants.IMAGES_URL + message.student_image)
                            .into(profileIv)
                    } else{
                        profileIv.setImageResource(R.drawable.message_profile)
                    }
                } else{
                    nameTxt.text = "Group Message"
                }
                dateTxt.text = message.date
                messageTxt.text = message.message
                if (message.read_status.equals("0", true)){
                    readImg.visibility = View.VISIBLE
                    messageTxt.setTypeface(null, Typeface.BOLD)
                }else{
                    readImg.visibility = View.GONE
                    messageTxt.setTypeface(null, Typeface.NORMAL)
                }

                if (message.image.isNotEmpty()) {
                    fileImg.visibility = View.VISIBLE
                    if (message.image.endsWith(".pdf")) {
                        fileImg.setImageResource(R.drawable.file_img)
                    } else {
                        fileImg.setImageResource(R.drawable.image_icon)
                    }
                } else {
                    fileImg.visibility = View.GONE
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun showLoadingFooter() {
        messages.add(null)
        notifyItemInserted(messages.size - 1)
    }

    fun removeLoadingFooter() {
        val index = messages.indexOf(null)
        if (index != -1) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}