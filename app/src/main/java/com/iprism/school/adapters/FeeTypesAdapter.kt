package com.iprism.school.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.FeeItemBinding
import com.iprism.school.model.fees.FeeTypesItem
import com.iprism.school.viewholders.FeeItemViewHolder

class FeeTypesAdapter(private var context: Context, private var feeTypesItems : List<FeeTypesItem>) : RecyclerView.Adapter<FeeItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeeItemViewHolder {
        val binding = FeeItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FeeItemViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FeeItemViewHolder, position: Int) {
        val feeType = feeTypesItems[position]
        holder.binding.feeTypeTxt.text = feeType.category
        holder.binding.feeIdTxt.text = "Fee Id : " + feeType.feeId
        holder.binding.feeAmountTxt.text = "₹" + feeType.totalFee.toString()
    }

    override fun getItemCount(): Int {
        return feeTypesItems.size
    }
}