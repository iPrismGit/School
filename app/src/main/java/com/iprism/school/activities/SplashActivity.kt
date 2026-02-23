package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.MainThread
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivitySplashBinding
import com.iprism.school.utils.User

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        Log.d("newUserDetails", user!!.getNewUserDetails().toString())
        Log.d("authToken", userDetails[User.AUTH_TOKEN].toString())
        val handler = Handler()
        handler.postDelayed({
            if (user!!.isUserLoggedIn()) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }

}