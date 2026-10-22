package com.example.tif_dimas.Profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tif_dimas.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil username yang disimpan saat login
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")
        binding.tvLoginAs.text = "Login sebagai: $username"

        binding.btnBack.setOnClickListener { finish() }
    }
}