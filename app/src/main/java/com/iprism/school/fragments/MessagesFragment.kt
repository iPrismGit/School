package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.school.R
import com.iprism.school.activities.MessageActivity
import com.iprism.school.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private lateinit var binding : FragmentMessagesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater)
        handleMessageBtn()
        return binding.root
    }

    private fun handleMessageBtn() {
        binding.messageBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, MessageActivity::class.java))
        })
    }
}