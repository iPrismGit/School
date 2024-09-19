package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.FoodItemBinding
import com.iprism.school.interfaces.OnFoodClickListener

class FoodItemsAdapter(context: Context) :
    RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>() {

    private lateinit var onFoodClickListener: OnFoodClickListener

    fun setListener(listener: OnFoodClickListener) {
        this.onFoodClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemsAdapter.FoodItemViewHolder {
        var binding: FoodItemBinding =
            FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemsAdapter.FoodItemViewHolder, position: Int) {
        holder.binding.root.setOnClickListener(View.OnClickListener {
            onFoodClickListener.onFoodItemClick(position.toString())
        })

        holder.binding.informationIv.setOnClickListener(View.OnClickListener {
            onFoodClickListener.onFoodInfoClick(position.toString())
        })
    }

    override fun getItemCount(): Int {
        return 5
    }

    class FoodItemViewHolder(var binding: FoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}