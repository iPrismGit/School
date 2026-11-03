package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.PlannerViewItemBinding
import com.iprism.school.interfaces.OnPlannerClickListener
import com.iprism.school.model.plannersandresources.Pdf
import com.iprism.school.utils.Constants

class PlannerDetailsAdapter(var context: Context, var pdfs: List<Pdf>) :
    RecyclerView.Adapter<PlannerDetailsAdapter.PlannerDetailsVideHolder>() {

    private lateinit var listener: OnPlannerClickListener

    fun setupListener(listener: OnPlannerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlannerDetailsAdapter.PlannerDetailsVideHolder {
        val binding =
            PlannerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerDetailsVideHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlannerDetailsAdapter.PlannerDetailsVideHolder,
        position: Int
    ) {
        val pdf = pdfs[position]
        if (!pdf.image.endsWith(".pdf")) {
            Glide.with(holder.binding.image.context).load(Constants.IMAGES_URL + pdf.image).into(holder.binding.image)
        } else {
            holder.binding.image.setImageResource(R.drawable.document_icon)
        }
        holder.binding.root.setOnClickListener {
            listener.onViewClick(pdf.image)
        }

    }

    override fun getItemCount(): Int {
        return pdfs.size
    }

    class PlannerDetailsVideHolder(var binding: PlannerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}