package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.GroupsAdapter
import com.iprism.school.databinding.ActivityGroupsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.interfaces.OnGroupItemClickListener

class GroupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupGroupsAdapter()
        handleAddBtn()
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateGroupActivity::class.java))
        })
    }

    private fun setupGroupsAdapter() {
        var  groupsAdapter = GroupsAdapter(this)
        binding.groupsRv.adapter = groupsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.groupsRv.layoutManager = linearLayoutManager
        groupsAdapter.setListener(object : OnGroupItemClickListener{
            override fun onItemClick(id: String) {
                var intent = Intent(this@GroupsActivity, GroupDetailsActivity::class.java)
                startActivity(intent)
            }

        })
    }

}