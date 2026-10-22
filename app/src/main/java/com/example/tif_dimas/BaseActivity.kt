package com.example.tif_dimas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tif_dimas.Beranda.BerandaFragment
import com.example.tif_dimas.BibitAfkir.BibitAfkirFragment
import com.example.tif_dimas.databinding.ActivityBaseBinding
import com.example.tif_dimas.JenisBibit.JenisBibitFragment
import com.example.tif_dimas.Profile.ProfilFragment
import com.example.tif_dimas.stokbibit.StokBibitFragment

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // bottom = 0 agar BottomNavigation tidak memiliki jarak di bagian bawah
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        /** BerandaFragment sebagai fragment default */
        if (savedInstanceState == null) {
            replaceFragment(BerandaFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_beranda -> {
                    replaceFragment(BerandaFragment())
                    true
                }
                R.id.nav_stok -> {
                    replaceFragment(StokBibitFragment())
                    true
                }
                R.id.nav_jenis -> {
                    replaceFragment(JenisBibitFragment())
                    true
                }
                R.id.nav_afkir -> {
                    replaceFragment(BibitAfkirFragment())
                    true
                }
                R.id.nav_profil -> {
                    replaceFragment(ProfilFragment())
                    true
                }
                else -> false // return false jika item tidak ada yang di klik
            }
        }

        // Menu yang sedang aktif diklik lagi -> tidak perlu memuat ulang fragment
        binding.bottomNavView.setOnItemReselectedListener { }
    }

    /** Dipanggil dari fragment untuk berpindah menu (misal dari kartu ringkasan di Beranda) */
    fun bukaMenu(menuId: Int) {
        binding.bottomNavView.selectedItemId = menuId
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            //.addToBackStack(null) -> dinonaktifkan agar saat back langsung keluar aplikasi
            .commit()
    }
}