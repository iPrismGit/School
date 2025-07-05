package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ClassItemBinding
import com.iprism.school.databinding.FoodItemBinding
import com.iprism.school.interfaces.OnFoodClickListener
import com.iprism.school.model.Response.ClasseListrr
import com.iprism.school.model.Response.MealplannerList

class FoodItemsAdapter(
    var activity: Context,
    var response: List<MealplannerList>,
    var OnItemCallBack: ((MealplannerList)  ->Unit )? = null,
    var OnItemCallEdit: ((MealplannerList)  ->Unit )? = null)
    : Adapter<FoodItemsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: FoodItemBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: FoodItemBinding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.binding.mealNameTxt.text = response[position].meal_name.toString()
        holder.binding.dateTxt.text = response[position].date.toString()
        holder.binding.dayTxt.text = response[position].day.toString()

//        Glide.with(activity)
//            .load(Constants.IMAGES_URL+response[position].attachment)
//            .into(holder.binding.priPic)

//        holder.itemView.setOnClickListener{
//            OnItemCallEdit!!.invoke(response[position])
//        }

        holder.itemView.setOnClickListener{
            OnItemCallBack!!.invoke(response[position])
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }
}