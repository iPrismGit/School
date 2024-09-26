package com.iprism.school.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityDaycareEmailReportBinding
import com.iprism.school.databinding.CreatedDiaryItemBinding
import com.iprism.school.interfaces.OnCreatedDiariesClickListener

class CreatedDiariesAdapter(var context: Context) :
    Adapter<CreatedDiariesAdapter.CreatedDairyViewHolder>() {

    private lateinit var listener: OnCreatedDiariesClickListener

    public fun setListener(listener: OnCreatedDiariesClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreatedDiariesAdapter.CreatedDairyViewHolder {
        var binding =
            CreatedDiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreatedDairyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CreatedDiariesAdapter.CreatedDairyViewHolder,
        position: Int
    ) {
        holder.binding.subjectTxt.text = "Maths $position"
        holder.binding.workTypeTxt.text = "Class Work $position"
        holder.binding.deleteIv.setOnClickListener(View.OnClickListener {
            listener.onDeleteClickListener(position)
        })

        holder.binding.infoIv.setOnClickListener(View.OnClickListener {
            listener.onInformationClickListener(position)
        })
    }

    override fun getItemCount(): Int {
        return 10
    }

    class CreatedDairyViewHolder(var binding: CreatedDiaryItemBinding) : ViewHolder(binding.root) {

    }

}