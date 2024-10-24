package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.activities.MessageActivity
import com.iprism.school.activities.MessageDetailsActivity
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.databinding.FragmentMessagesBinding
import com.iprism.school.interfaces.OnMessageClickListener

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private var tag: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater)
        handleMessageBtn()
        tag = arguments?.getString("tag").toString()
        setupFragmentSettings(tag)
        setupMessagesAdapter()
        return binding.root
    }

    private fun setupMessagesAdapter() {
        var messagesAdapter = MessagesAdapter(requireContext())
        binding.messagesRv.adapter = messagesAdapter
        var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.messagesRv.layoutManager = linearLayoutManager
        messagesAdapter.setupListener(object : OnMessageClickListener{
            override fun onItemClick(messageId: String) {
                var intent = Intent(requireContext(), MessageDetailsActivity::class.java)
                intent.putExtra("messageId", messageId)
                intent.putExtra("tag", tag)
                startActivity(intent)
            }

        })
    }

    private fun setupFragmentSettings(tag: String) {
        if (tag.equals("msg", true)) {
            binding.textView10.text = "Messages"
        } else if (tag.equals("msgInbox", true)) {
            binding.textView10.text = "Messages"
        } else if (tag.equals("sent", true)) {
            binding.textView10.text = "Sent Messages"
        } else if (tag.equals("scheduled", true)) {
            binding.textView10.text = " Scheduled Messages"
        }
    }

    private fun handleMessageBtn() {
        binding.messageBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, MessageActivity::class.java))
        })
    }
}