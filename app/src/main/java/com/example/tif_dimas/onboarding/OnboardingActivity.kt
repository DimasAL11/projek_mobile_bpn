package com.example.tif_dimas.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.tif_dimas.BaseActivity
import com.example.tif_dimas.auth.AuthActivity
import com.example.tif_dimas.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup ViewPager2 dengan adapter
        val fragmentsList = listOf(Onboarding1Fragment(), Onboarding2Fragment(), Onboarding3Fragment())
        val adapter = OnboardingAdapter(this, fragmentsList)
        binding.onboardingViewPager.adapter = adapter

        // Hubungkan DotsIndicator dengan ViewPager2
        binding.dotIndicator.attachTo(binding.onboardingViewPager)

        // Halaman terakhir: tombol berubah jadi "Mulai" dan tombol Lewati disembunyikan
        binding.onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val halamanTerakhir = position == fragmentsList.lastIndex
                binding.btnLanjut.text = if (halamanTerakhir) "Mulai" else "Lanjut"
                binding.btnLewati.alpha = if (halamanTerakhir) 0f else 1f
                binding.btnLewati.isEnabled = !halamanTerakhir
            }
        })

        binding.btnLanjut.setOnClickListener {
            val posisi = binding.onboardingViewPager.currentItem
            if (posisi < fragmentsList.lastIndex) {
                binding.onboardingViewPager.currentItem = posisi + 1
            } else {
                selesai()
            }
        }
        binding.btnLewati.setOnClickListener { selesai() }
    }

    private fun selesai() {
        // Dibuka dari Beranda (menu Tutorial) -> cukup tutup dan kembali ke Beranda
        if (intent.getBooleanExtra(EXTRA_DARI_BERANDA, false)) {
            finish()
            return
        }

        // Dibuka pertama kali dari Splash -> tandai sudah dilihat lalu lanjut ke alur login
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_SELESAI, true).apply()

        val isLogin = getSharedPreferences("user_pref", MODE_PRIVATE).getBoolean("isLogin", false)
        val target = if (isLogin) BaseActivity::class.java else AuthActivity::class.java
        startActivity(Intent(this, target))
        finish()
    }

    companion object {
        // Pref terpisah dari user_pref agar tidak terhapus saat logout
        const val PREF_NAME = "onboarding_pref"
        const val KEY_SELESAI = "onboarding_done"
        const val EXTRA_DARI_BERANDA = "dari_beranda"
    }
}
