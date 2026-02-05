package com.iprism.school.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.viewholders.ItemLoadingViewHolder
import com.iprism.school.databinding.HelpTutorialItemBinding
import com.iprism.school.databinding.ItemLoadingBinding
import com.iprism.school.model.helptutorials.HelpTutorial
import com.iprism.school.utils.Constants

class HelpTutorialAdapter(private val helpTutorials: ArrayList<HelpTutorial?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HelpTutorialViewHolder(val binding: HelpTutorialItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (helpTutorials[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            HelpTutorialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding =
            ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == HelpTutorialAdapter.Companion.VIEW_TYPE_ITEM) {
            HelpTutorialViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is HelpTutorialViewHolder) {
            val tutorial = helpTutorials[position]
            val context = holder.itemView.context
            holder.binding.apply {
                titleTxt.text = tutorial!!.title
                descriptionTxt.text = tutorial.description
                if (tutorial.image.isNotEmpty()) {
                    Glide.with(context)
                        .load(Constants.IMAGES_URL + tutorial.image)
                        .into(tutorialImg)
                }
                watchBtn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tutorial.link))
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return helpTutorials.size
    }

    fun showLoadingFooter() {
        helpTutorials.add(null)
        notifyItemInserted(helpTutorials.size - 1)
    }

    fun removeLoadingFooter() {
        val index = helpTutorials.indexOf(null)
        if (index != -1) {
            helpTutorials.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}