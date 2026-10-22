package com.example.tif_dimas.Beranda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tif_dimas.BaseActivity
import com.example.tif_dimas.Beranda.Activity1
import com.example.tif_dimas.R
import com.example.tif_dimas.beranda.pertemuan7.SeventhActivity
import com.example.tif_dimas.databinding.FragmentBerandaBinding

class BerandaFragment : Fragment() {
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment harus memakai (requireActivity() as AppCompatActivity) untuk Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Beranda"
        }

        // Tampilkan username yang tersimpan saat login
        // (requireContext() karena sedang berada di Fragment)
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")
        binding.tvWelcome.text = "Halo, $username!"

        // Kartu ringkasan -> pindah ke menu bottom navigation yang sesuai
        binding.cardStok.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_stok)
        }
        binding.cardJenis.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_jenis)
        }
        binding.cardAfkir.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_afkir)
        }

        // Tombol praktikum sebelumnya (this -> requireContext())
        binding.btnToKripik.setOnClickListener {
            startActivity(Intent(requireContext(), Activity1::class.java))
        }
        binding.btnToSeventh.setOnClickListener {
            startActivity(Intent(requireContext(), SeventhActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}