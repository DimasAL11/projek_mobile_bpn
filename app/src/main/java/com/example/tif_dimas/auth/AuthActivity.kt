package com.example.tif_dimas.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tif_dimas.BaseActivity
import com.example.tif_dimas.databinding.ActivityAuthBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var biometricHelper: BiometricHelper

    // Login lama (P6) dengan akun hardcode dimas / 123456 sudah diganti aturan Quiz P8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // register_pref = daftar akun hasil registrasi (tidak ikut terhapus saat logout)
        val accountStorage = AccountStorage(this)
        // Fitur baru: dibuat di onCreate karena BiometricPrompt harus dibuat sebelum dipakai
        biometricHelper = BiometricHelper(this)

        // Soal 1: tombol Register With Gmail -> halaman input email
        binding.btnRegisterGmail.setOnClickListener {
            startActivity(Intent(this, GmailEmailActivity::class.java))
        }

        // Fitur baru: login dengan sidik jari
        binding.btnBiometric.setOnClickListener {
            val username = biometricHelper.getSavedUsername() ?: return@setOnClickListener
            biometricHelper.authenticate(
                title = "Login Sidik Jari",
                subtitle = "Masuk sebagai $username",
                onSuccess = { bukaHome(username) },
                onError = { pesan -> showErrorDialog("Login Sidik Jari Gagal", pesan) }
            )
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
                // Cek 2: memenuhi salah satu kondisi -> tawarkan sidik jari, lalu ke Home
                loginSamaPassword || loginAkunTerdaftar -> {
                    tawarkanSidikJari(username)
                }
                // Cek 3: tidak memenuhi keduanya
                else -> {
                    showErrorDialog("Login Gagal", "Username atau password salah.")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateTombolSidikJari()
    }

    /** Tombol sidik jari hanya tampil jika HP mendukung DAN fitur sudah diaktifkan */
    private fun updateTombolSidikJari() {
        val savedUsername = biometricHelper.getSavedUsername()
        val bisaDipakai = biometricHelper.isAvailable() && savedUsername != null

        binding.btnBiometric.visibility = if (bisaDipakai) View.VISIBLE else View.GONE
        if (bisaDipakai) {
            binding.btnBiometric.text = "Masuk dengan Sidik Jari ($savedUsername)"
        }
    }

    /** Setelah login biasa berhasil: tawarkan aktivasi sidik jari (sekali saja) */
    private fun tawarkanSidikJari(username: String) {
        // Tidak ditawarkan jika HP tidak mendukung atau akun ini sudah aktif
        if (!biometricHelper.isAvailable() || biometricHelper.isEnabledFor(username)) {
            bukaHome(username)
            return
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Aktifkan Login Sidik Jari?")
            .setMessage("Lain kali kamu bisa masuk sebagai \"$username\" cukup dengan sidik jari, tanpa mengetik password.")
            .setCancelable(false)
            .setPositiveButton("Aktifkan") { _, _ ->
                // Verifikasi dulu bahwa pemilik sidik jari yang mengaktifkan
                biometricHelper.authenticate(
                    title = "Konfirmasi Sidik Jari",
                    subtitle = "Aktifkan login sidik jari untuk $username",
                    onSuccess = {
                        biometricHelper.enable(username)
                        Toast.makeText(this, "Login sidik jari aktif", Toast.LENGTH_SHORT).show()
                        bukaHome(username)
                    },
                    onError = { bukaHomeTanpaSidikJari(username) },
                    onCancel = { bukaHomeTanpaSidikJari(username) }
                )
            }
            .setNegativeButton("Nanti") { _, _ -> bukaHome(username) }
            .show()
    }

    private fun bukaHomeTanpaSidikJari(username: String) {
        Toast.makeText(this, "Login sidik jari bisa diaktifkan di menu Profil", Toast.LENGTH_SHORT).show()
        bukaHome(username)
    }

    /** Simpan sesi (user_pref) lalu masuk ke BaseActivity (Home) */
    private fun bukaHome(username: String) {
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("username", username)
        editor.apply()

        startActivity(Intent(this, BaseActivity::class.java))
        finish()
    }
}