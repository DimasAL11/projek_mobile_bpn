package com.example.tif_dimas.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tif_dimas.databinding.ActivityGmailEmailBinding

/** Soal 1 - halaman input email untuk Register With Gmail */
class GmailEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGmailEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGmailEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        val accountStorage = AccountStorage(this)

        binding.btnLanjut.setOnClickListener {
            // Simpan dalam huruf kecil agar "Budi@Gmail.com" dan "budi@gmail.com" dianggap sama
            val email = binding.etEmail.text.toString().trim().lowercase()

            when {
                // Validasi 1: email tidak boleh kosong
                email.isEmpty() -> {
                    showErrorDialog("Email Kosong", "Email tidak boleh kosong.")
                }
                // Validasi 2: email harus menggunakan domain @gmail.com
                !isGmail(email) -> {
                    showErrorDialog(
                        "Email Tidak Valid",
                        "Email harus menggunakan domain @gmail.com.\nContoh: nama@gmail.com"
                    )
                }
                // Validasi tambahan: satu email hanya untuk satu akun
                accountStorage.isEmailTaken(email) -> {
                    showErrorDialog("Email Sudah Terdaftar", "Email ini sudah terdaftar, silakan login.")
                }
                // Valid: buka halaman Registrasi dan bawa email-nya
                else -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.putExtra(RegisterActivity.EXTRA_EMAIL, email)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    /** Format email harus benar DAN diakhiri @gmail.com (contoh: nama@gmail.com) */
    private fun isGmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com")
    }
}