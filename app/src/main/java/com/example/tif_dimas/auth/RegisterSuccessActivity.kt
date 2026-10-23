package com.example.tif_dimas.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tif_dimas.databinding.ActivityRegisterSuccessBinding

/** Halaman informasi "Registrasi Berhasil" */
class RegisterSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Data akun yang baru saja didaftarkan, dikirim dari RegisterActivity
        val nama = intent.getStringExtra(EXTRA_NAMA).orEmpty()
        val email = intent.getStringExtra(EXTRA_EMAIL).orEmpty()
        val username = intent.getStringExtra(EXTRA_USERNAME).orEmpty()

        binding.tvNama.text = "Nama: $nama"
        binding.tvEmail.text = "Email: $email"
        binding.tvUsername.text = "Username: $username"

        // Jumlah semua akun yang tersimpan di SharedPreferences
        val totalAkun = AccountStorage(this).countAccounts()
        binding.tvTotalAkun.text = "Total akun terdaftar: $totalAkun"

        // Kembali ke AuthActivity (halaman Login) yang ada di bawah halaman ini
        binding.btnToLogin.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_NAMA = "EXTRA_NAMA"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }
}