package com.iprism.school.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ItemChatReceivedBinding
import com.iprism.school.databinding.ItemChatSentBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.messagemodel.MessagesItem
import com.iprism.school.utils.Constants
import com.iprism.school.viewholders.ItemLoadingViewHolder
import kotlin.text.equals
import kotlin.text.isNotEmpty
import kotlin.text.replaceFirstChar
import kotlin.text.uppercase

class ChatAdapter(private val messages: ArrayList<MessagesItem?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: OnMessageClickListener

    fun setupListener(listener: OnMessageClickListener) {
        this.listener = listener
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {

        val item = messages[position]

        return when {
            item == null -> VIEW_TYPE_LOADING
            item.senderType.equals("teacher", true) -> VIEW_TYPE_SENT
            else -> VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemChatSentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            SentViewHolder(binding)
        } else if (viewType == VIEW_TYPE_RECEIVED) {
            val binding = ItemChatReceivedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ReceivedViewHolder(binding)
        } else {
            val binding = ItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ItemLoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentViewHolder).bind(message!!)
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED) {
            (holder as ReceivedViewHolder).bind(message!!)
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SentViewHolder(private val binding: ItemChatSentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessagesItem) {

            Log.d("CHAT_IMAGE", "Sent Image: ${message.image}")

            binding.tvMessage.text = message.message
            binding.tvTime.text = message.date

            if (!message.image.isNullOrEmpty()) {
                binding.messageImg.visibility = View.VISIBLE

                if (message.image.endsWith(".pdf")) {
                    binding.messageImg.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.file_img
                        )
                    )
                } else {
                    Glide.with(binding.root.context)
                        .load(Constants.IMAGES_URL + message.image)
                        .into(binding.messageImg)
                }
            } else {
                binding.messageImg.visibility = View.GONE
            }
            binding.root.setOnClickListener { view ->
                listener.onInnerItemClick(message.image)
            }
        }
    }

    inner class ReceivedViewHolder(private val binding: ItemChatReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessagesItem) {
            binding.tvMessage.text = message.message
            binding.tvTime.text = message.date
            if (!message.image.isNullOrEmpty()) {
                binding.messageImg.visibility = View.VISIBLE

                if (message.image.endsWith(".pdf")) {
                    binding.messageImg.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.file_img
                        )
                    )
                } else {
                    Glide.with(binding.root.context)
                        .load(Constants.IMAGES_URL + message.image)
                        .into(binding.messageImg)
                }
            } else {
                binding.messageImg.visibility = View.GONE
            }
            binding.tvSenderName.text = message.senderType.replaceFirstChar { it.uppercase() }
            binding.root.setOnClickListener { view ->
                listener.onInnerItemClick(message.image)
            }
        }
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
