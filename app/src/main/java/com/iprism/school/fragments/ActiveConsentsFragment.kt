package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.activities.SingleConsentActivity
import com.iprism.school.adapters.ConsentsAdapter
import com.iprism.school.databinding.FragmentActiveConsentsBinding
import com.iprism.school.interfaces.OnConsentClickListener


class ActiveConsentsFragment : Fragment() {

    private lateinit var binding: FragmentActiveConsentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveConsentsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
      val consentsAdapter  = ConsentsAdapter(requireContext())
        binding.activeConsentsRv.adapter = consentsAdapter
        var layoutManager = LinearLayoutManager(requireContext())
        binding.activeConsentsRv.layoutManager = layoutManager
        consentsAdapter.setListener( object : OnConsentClickListener{
            override fun onConsentItemClickListener(consentId: Int) {
                val intent = Intent(context, SingleConsentActivity::class.java)
                intent.putExtra("consentId", consentId);
                startActivity(intent)
            }
        })
    }

}