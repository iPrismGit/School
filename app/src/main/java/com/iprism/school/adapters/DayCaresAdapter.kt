package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.DayCareItemBinding
import com.iprism.school.databinding.FragmentDayCareBinding
import com.iprism.school.interfaces.OnDayCareClickListener

class DayCaresAdapter(var context: Context) : Adapter<DayCaresAdapter.DayCareViewHolder>() {


    private lateinit var listener: OnDayCareClickListener

    public fun setupListener(listener: OnDayCareClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayCaresAdapter.DayCareViewHolder {
        var binding = DayCareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayCareViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayCaresAdapter.DayCareViewHolder, position: Int) {
        holder.binding.dayCareNameTxt.text = "Snack Time-$position"
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemLick(position.toString(), holder.binding.dayCareNameTxt.text.toString())
        })
    }

    override fun getItemCount(): Int {
        return 5
    }

    class DayCareViewHolder(var binding: DayCareItemBinding) : ViewHolder(binding.root) {

    }

}