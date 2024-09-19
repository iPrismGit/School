package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.FoodItemBinding

class FoodItemsAdapter(context: Context) :
    RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodItemsAdapter.FoodItemViewHolder {
        var binding: FoodItemBinding =
            FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemsAdapter.FoodItemViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class FoodItemViewHolder(var binding: FoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}