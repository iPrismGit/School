package com.iprism.school.fragments

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.school.R
import com.iprism.school.activities.DaycareReportActivity
import com.iprism.school.adapters.DayCaresAdapter
import com.iprism.school.databinding.FragmentDayCareBinding
import com.iprism.school.interfaces.OnDayCareClickListener

class DayCareFragment : Fragment() {

    private lateinit var binding: FragmentDayCareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayCareBinding.inflate(inflater, container, false)
        setupDayCareAdapter()
        return binding.root
    }

    private fun setupDayCareAdapter() {
        var dayCaresAdapter = DayCaresAdapter(requireContext())
        binding.dayCareRv.adapter = dayCaresAdapter
        var linearLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.dayCareRv.layoutManager = linearLayoutManager
        dayCaresAdapter.setupListener(object : OnDayCareClickListener{
            override fun onItemLick(id: String, name: String) {
                var intent = Intent(context, DaycareReportActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("name", name)
                startActivity(intent)
            }
        })
    }

}