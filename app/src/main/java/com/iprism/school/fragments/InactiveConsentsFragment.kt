package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.school.databinding.FragmentInactiveConsentsBinding

class InactiveConsentsFragment : Fragment() {

    private lateinit var binding: FragmentInactiveConsentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInactiveConsentsBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    private fun setupAdapter() {
//        val consentsAdapter  = ConsentsAdapter(requireContext())
//        binding.inactiveConsentsRv.adapter = consentsAdapter
//        var layoutManager = LinearLayoutManager(requireContext())
//        binding.inactiveConsentsRv.layoutManager = layoutManager
//        consentsAdapter.setListener(object : OnConsentClickListener{
//            override fun onConsentItemClickListener(consentId: Int) {
//                val intent = Intent(context, SingleConsentActivity::class.java)
//                intent.putExtra("consentId", consentId);
//                startActivity(intent)
//            }
//
//        })
    }

}