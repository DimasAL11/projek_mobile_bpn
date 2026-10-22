package com.example.tif_dimas.BibitAfkir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tif_dimas.databinding.FragmentBibitAfkirBinding

class BibitAfkirFragment : Fragment() {
    private var _binding: FragmentBibitAfkirBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBibitAfkirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment harus memakai (requireActivity() as AppCompatActivity) untuk Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Bibit Afkir"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}