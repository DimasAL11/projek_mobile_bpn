package com.example.tif_dimas.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tif_dimas.BaseActivity
import com.example.tif_dimas.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        lifecycleScope.launch {
            delay(2000) // simulasi pengambilan data 2 detik

            val isLogin = sharedPref.getBoolean("isLogin", false)
            val target = if (isLogin) BaseActivity::class.java
            else AuthActivity::class.java

            startActivity(Intent(this@SplashScreenActivity, target))
            finish()
        }
    }
}