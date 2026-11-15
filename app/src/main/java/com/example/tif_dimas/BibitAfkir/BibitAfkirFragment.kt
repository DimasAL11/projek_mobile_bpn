package com.example.tif_dimas.BibitAfkir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tif_dimas.R
import com.example.tif_dimas.data.DataBibit
import com.example.tif_dimas.databinding.FragmentBibitAfkirBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

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
        // Fitur baru: grafik jumlah afkir per penyebab
        setupBarChart()
    }
    /** Bar chart jumlah bibit afkir per penyebab (MPAndroidChart) */
    private fun setupBarChart() {
        val daftarAfkir = DataBibit.daftarAfkir

        // 1. Satu BarEntry per penyebab: x = urutan (0, 1, 2, 3), y = jumlah bibit
        val entries = daftarAfkir.mapIndexed { index, afkir ->
            BarEntry(index.toFloat(), afkir.jumlah.toFloat())
        }

        // 2. DataSet + gaya batang
        val dataSet = BarDataSet(entries, "Jumlah bibit afkir").apply {
            color = ContextCompat.getColor(requireContext(), R.color.status_danger)
            valueTextSize = 12f
            // angka di atas batang tanpa desimal: 45 bukan 45.0
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = value.toInt().toString()
            }
        }

        // 3. Pasang data ke chart dan atur sumbu
        val barChart = binding.barChartAfkir
        barChart.data = BarData(dataSet).apply { barWidth = 0.6f }
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
        barChart.setTouchEnabled(false)

        // Sumbu X: label penyebab di bawah tiap batang
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(daftarAfkir.map { it.labelGrafik })
        }
        // Sumbu Y kiri mulai dari 0, sumbu kanan disembunyikan
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.granularity = 1f
        barChart.axisRight.isEnabled = false

        barChart.animateY(800)
        barChart.invalidate()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}