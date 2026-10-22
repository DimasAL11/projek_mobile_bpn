package com.example.tif_dimas.JenisBibit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tif_dimas.Beranda.WebViewActivity
import com.example.tif_dimas.databinding.FragmentJenisBibitBinding

class JenisBibitFragment : Fragment() {
    private var _binding: FragmentJenisBibitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJenisBibitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment harus memakai (requireActivity() as AppCompatActivity) untuk Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Jenis Bibit"
        }

        // Buka halaman WebView berisi info benih unggul
        binding.btnInfoBenih.setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}