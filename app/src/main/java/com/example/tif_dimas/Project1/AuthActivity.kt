package com.example.tif_dimas.Project1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tif_dimas.MainActivity
import com.example.tif_dimas.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    // Kredensial yang dianggap benar
    private val validUsername = "dimas"
    private val validPassword = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            when {
                // Cek 1: kolom kosong
                username.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show()
                }
                // Cek 2: username dan password cocok -> simpan isLogin
                username == validUsername && password == validPassword -> {
                    val editor = sharedPref.edit()
                    editor.putBoolean("isLogin", true)
                    editor.putString("username", username)
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                // Cek 3: tidak cocok
                else -> {
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}