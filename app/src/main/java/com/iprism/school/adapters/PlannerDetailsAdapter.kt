package com.iprism.school.adapters

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.PlannerViewItemBinding
import com.iprism.school.model.plannersandresources.Pdf

class PlannerDetailsAdapter(var context: Context, var pdfs : List<Pdf>) : RecyclerView.Adapter<PlannerDetailsAdapter.PlannerDetailsVideHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlannerDetailsAdapter.PlannerDetailsVideHolder {
        var binding = PlannerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerDetailsVideHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlannerDetailsAdapter.PlannerDetailsVideHolder,
        position: Int
    ) {
        var pdf = pdfs[position]
        var itemName = pdf.image
        val spannable = SpannableString(itemName)
        spannable.setSpan(
            UnderlineSpan(),
            0,
            itemName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.binding.linkTxt.text = spannable
        holder.binding.viewIv.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return pdfs.size
    }

    class PlannerDetailsVideHolder(var binding: PlannerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

}