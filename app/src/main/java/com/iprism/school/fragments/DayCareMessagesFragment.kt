package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateDayCareAlbumsBinding
import com.iprism.school.databinding.FragmentDayCareMessagesBinding

class DayCareMessagesFragment : Fragment() {

    private lateinit var binding: FragmentDayCareMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayCareMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }


}