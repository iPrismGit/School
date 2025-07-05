package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.school.databinding.ActivityCreateClassSubjectsBinding
import com.iprism.school.databinding.ActivityEditRightsBinding
import com.iprism.school.databinding.ClassItemBinding
import com.iprism.school.databinding.ClassSubjectItemBinding
import com.iprism.school.interfaces.OnSubjectClickListener
import com.iprism.school.model.Response.ClasseListrr
import com.iprism.school.model.Response.SubjectTcharList

class ClassSubjectsAdapter(
    var activity: Context,
    var response: List<SubjectTcharList>,
    var OnItemCallBack: ((SubjectTcharList)  ->Unit )? = null,
    var OnItemCallEdit: ((SubjectTcharList)  ->Unit )? = null)
    : Adapter<ClassSubjectsAdapter.ViewHolders>() {

    var postionstaus = 1

    class ViewHolders(var binding: ClassSubjectItemBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding: ClassSubjectItemBinding = ClassSubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }


    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.binding.subjectTv.text = response[position].subject_name.toString()
        holder.binding.teacherNameTv.text = response[position].teachers.toString()

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