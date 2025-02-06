package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {


    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("userDetails", user!!.getUserDetails().toString())
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
            //    startActivity(Intent(this, LocationActivity::class.java))
        }, 2000)
    }

}