package com.example.tif_dimas.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tif_dimas.auth.AccountStorage
import com.example.tif_dimas.databinding.ActivityRegisterBinding

/** Soal 2 - halaman Registrasi: Nama, Email (terkunci), Username, Password */
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var accountStorage: AccountStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        accountStorage = AccountStorage(this)

        // Email otomatis terbawa dari GmailEmailActivity
        val email = intent.getStringExtra(EXTRA_EMAIL)
        if (email.isNullOrBlank()) {
            // Halaman ini hanya boleh dibuka lewat Register With Gmail
            finish()
            return
        }
        binding.etEmail.setText(email)
        binding.etEmail.isEnabled = false // email tidak boleh diubah

        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                // Validasi 1: semua field wajib diisi
                nama.isEmpty() || username.isBlank() || password.isEmpty() -> {
                    showErrorDialog("Registrasi Gagal", "Semua field wajib diisi.")
                }
                // Validasi 2: username tidak boleh mengandung spasi
                username.any { it.isWhitespace() } -> {
                    showErrorDialog("Registrasi Gagal", "Username tidak boleh mengandung spasi.")
                }
                // Validasi 3: password minimal 6 karakter
                password.length < 6 -> {
                    showErrorDialog("Registrasi Gagal", "Password minimal 6 karakter.")
                }
                // Validasi tambahan: username harus unik karena akun bisa lebih dari satu
                accountStorage.isUsernameTaken(username) -> {
                    showErrorDialog("Registrasi Gagal", "Username \"$username\" sudah dipakai akun lain.")
                }
                // Lolos validasi: simpan ke SharedPreferences lalu ke halaman Registrasi Berhasil
                else -> {
                    accountStorage.addAccount(Account(nama, email, username, password))

                    val intent = Intent(this, RegisterSuccessActivity::class.java)
                    intent.putExtra(RegisterSuccessActivity.EXTRA_NAMA, nama)
                    intent.putExtra(RegisterSuccessActivity.EXTRA_EMAIL, email)
                    intent.putExtra(RegisterSuccessActivity.EXTRA_USERNAME, username)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
    }
}