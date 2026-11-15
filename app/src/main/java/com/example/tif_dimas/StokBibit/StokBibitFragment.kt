package com.example.tif_dimas.stokbibit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tif_dimas.data.DataBibit
import com.example.tif_dimas.databinding.FragmentStokBibitBinding

class StokBibitFragment : Fragment() {
    private var _binding: FragmentStokBibitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStokBibitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment harus memakai (requireActivity() as AppCompatActivity) untuk Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Stok Bibit"
        }

        // Ringkasan dihitung dari data, bukan diketik manual
        val daftarStok = DataBibit.daftarStok
        binding.tvRingkasan.text =
            "Total ${DataBibit.formatAngka(DataBibit.totalStok())} bibit di ${daftarStok.size} lokasi pembibitan"

        // RecyclerView: inisialisasi Adapter dan LayoutManager
        val adapter = StokBibitAdapter(daftarStok) { stok ->
            Toast.makeText(
                requireContext(),
                "${stok.lokasi}: ${DataBibit.formatAngka(stok.jumlah)} bibit ${stok.fase}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.rvStok.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}