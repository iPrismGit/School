package com.iprism.school.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.FoodCategoryItemBinding
import com.iprism.school.databinding.FoodItemBinding

class FoodTypesAdapter(context: Context) : Adapter<FoodTypesAdapter.FoodTypeViewHolder>(){

    private var selectedPosition : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypesAdapter.FoodTypeViewHolder {
        var binding = FoodCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodTypeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FoodTypesAdapter.FoodTypeViewHolder, position: Int) {
        holder.binding.textView44.text = "Food " + position
        if (selectedPosition == position){
        }

        holder.binding.root.setOnClickListener(View.OnClickListener {

        })
    }

    override fun getItemCount(): Int {
        return 5
    }


    class FoodTypeViewHolder(var binding: FoodCategoryItemBinding) : RecyclerView.ViewHolder(binding.root){

    }
}