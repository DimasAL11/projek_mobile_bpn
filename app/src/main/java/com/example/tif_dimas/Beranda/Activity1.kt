package com.example.tif_dimas.Beranda

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tif_dimas.R
import com.example.tif_dimas.databinding.Activity1Binding

class Activity1 : AppCompatActivity() {

    private lateinit var binding: Activity1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = Activity1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        // Inisialisasi komponen
//        val inputNama: EditText = findViewById(R.id.inputNama)
//        val btnSubmit: Button = findViewById(R.id.btnSubmit)
        // Tombol back pada toolbar
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnSubmit.setOnClickListener {
            //Mengambil value dari inputNama dan menampilkan di Logcat
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }
}