package com.example.tif_dimas.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tif_dimas.BaseActivity
import com.example.tif_dimas.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    // Login lama (P6) dengan akun hardcode dimas / 123456 sudah diganti aturan Quiz P8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // user_pref = sesi login (dihapus saat logout)
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        // register_pref = daftar akun hasil registrasi (tidak ikut terhapus saat logout)
        val accountStorage = AccountStorage(this)

        // Soal 1: tombol Register With Gmail -> halaman input email
        binding.btnRegisterGmail.setOnClickListener {
            startActivity(Intent(this, GmailEmailActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            // Kondisi 1: username sama dengan password
            val loginSamaPassword = username == password
            // Kondisi 2: username & password cocok dengan salah satu akun di SharedPreferences
            val loginAkunTerdaftar = accountStorage.findAccount(username, password) != null

            when {
                // Cek 1: kolom kosong
                username.isEmpty() || password.isEmpty() -> {
                    showErrorDialog("Login Gagal", "Username dan password wajib diisi.")
                }
                // Cek 2: memenuhi salah satu kondisi -> simpan sesi lalu ke Home
                loginSamaPassword || loginAkunTerdaftar -> {
                    val editor = sharedPref.edit()
                    editor.putBoolean("isLogin", true)
                    editor.putString("username", username)
                    editor.apply()

                    startActivity(Intent(this, BaseActivity::class.java))
                    finish()
                }
                // Cek 3: tidak memenuhi keduanya
                else -> {
                    showErrorDialog("Login Gagal", "Username atau password salah.")
                }
            }
        }
    }
}