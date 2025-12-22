package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivitySplashBinding
import com.iprism.school.utils.User

class SplashActivity : BaseActivity() {


    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

       val userId = userDetails[User.ID].toString()

        Log.d("userDetails", user!!.getUserDetails().toString())
        val handler = Handler()
        handler.postDelayed({
            if (userId == ""||userId == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            //    startActivity(Intent(this, LocationActivity::class.java))
        }, 2000)
    }

}