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
import com.iprism.school.model.dairy.Diary

class CreatedDiariesAdapter(var context: Context, var diaries: List<Diary>) : Adapter<CreatedDiariesAdapter.CreatedDairyViewHolder>() {

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
        var diary = diaries[position]
        if (diary.student_id.equals("all", true)) {
            holder.binding.sentNameTxt.text = "Sent to All Students"
        } else {
            holder.binding.sentNameTxt.text = "Sent to " + diary.first_name + " " + diary.middle_name + " " + diary.last_name
        }
        holder.binding.detailsTxt.text = diary.details
        if (diary.type.equals("cw", true)) {
            holder.binding.typeTxt.text = "Class Work"
        } else {
            holder.binding.typeTxt.text = "Home Work"
        }

        holder.binding.deleteIv.setOnClickListener { view ->
            listener.onDeleteClickListener(diary.id)
        }

        holder.binding.infoIv.setOnClickListener { view ->
            listener.onInformationClickListener(diary.student_id, diary.image, diary.type, diary.details, diary.first_name, diary.middle_name, diary.last_name)
        }

    }

    override fun getItemCount(): Int {
        return diaries.size
    }

    class CreatedDairyViewHolder(var binding: CreatedDiaryItemBinding) : ViewHolder(binding.root) {

    }

}