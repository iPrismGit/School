package com.iprism.school.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.databinding.HolidayItemBinding
import com.iprism.school.model.holidaysmodel.Holiday
import java.text.SimpleDateFormat
import java.util.Locale

class HolidaysAdapter(var context: Context, var holidays: List<Holiday>)  : RecyclerView.Adapter<HolidaysAdapter.HolidayViewHolder>() {

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolidaysAdapter.HolidayViewHolder {
        var binding = HolidayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolidayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HolidaysAdapter.HolidayViewHolder, position: Int) {
        var holiday = holidays[position]
        holder.binding.tvFullDay.text = holiday.day
        holder.binding.tvStatus.text = "Holiday"
        holder.binding.tvTitle.text = holiday.title
        setDateAndDay(holder, holiday.date)
    }

    override fun getItemCount(): Int {
        return holidays.size
    }

    private fun setDateAndDay(holder: HolidayViewHolder, dateStr: String) {
        try {
            val date = inputFormat.parse(dateStr) ?: return
            holder.binding.tvDate.text = dateFormat.format(date)
            holder.binding.tvDay.text = dayFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class HolidayViewHolder(var binding: HolidayItemBinding) : RecyclerView.ViewHolder(binding.root)
}