package com.example.tif_dimas.Beranda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tif_dimas.BaseActivity
import com.example.tif_dimas.Beranda.Activity1
import com.example.tif_dimas.R
import com.example.tif_dimas.Beranda.Pertemuan7.SeventhActivity
import com.example.tif_dimas.databinding.FragmentBerandaBinding
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.tif_dimas.data.DataBibit
import com.example.tif_dimas.onboarding.OnboardingActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

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
        // Tampilkan option menu (tombol Info) di Toolbar
        setHasOptionsMenu(true)

        // Tampilkan username yang tersimpan saat login
        // (requireContext() karena sedang berada di Fragment)
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")
        binding.tvWelcome.text = "Halo, $username!"
        // Angka ringkasan dihitung dari DataBibit
        binding.tvTotalStok.text = DataBibit.formatAngka(DataBibit.totalStok())
        binding.tvTotalAfkir.text = DataBibit.formatAngka(DataBibit.totalAfkir())
        binding.tvTotalJenis.text = DataBibit.daftarJenis.size.toString()

        // Fitur baru: grafik komposisi stok per fase
        setupPieChart()
        // Menu grid: kotak ringkasan -> pindah ke menu bottom navigation yang sesuai
        binding.cardStok.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_stok)
        }
        binding.cardJenis.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_jenis)
        }
        binding.cardAfkir.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_afkir)
        }

        // Menu grid: tutorial dan praktikum sebelumnya (this -> requireContext())
        binding.cardTutorial.setOnClickListener {
            bukaTutorial()
        }
        binding.cardSeventh.setOnClickListener {
            startActivity(Intent(requireContext(), SeventhActivity::class.java))
        }
        binding.cardKripik.setOnClickListener {
            startActivity(Intent(requireContext(), Activity1::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.beranda_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tutorial -> {
                bukaTutorial()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Buka Onboarding dari Beranda: tombol Mulai hanya menutup tutorial, tidak ke login */
    private fun bukaTutorial() {
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        intent.putExtra(OnboardingActivity.EXTRA_DARI_BERANDA, true)
        startActivity(intent)
    }
    /** Pie chart komposisi stok per fase (MPAndroidChart) */
    private fun setupPieChart() {
        // 1. Ubah data menjadi PieEntry: nilai = jumlah bibit, label = nama fase
        val entries = DataBibit.stokPerFase().map { (fase, jumlah) ->
            PieEntry(jumlah.toFloat(), fase)
        }

        // 2. DataSet = kumpulan entry + pengaturan tampilan potongan pie
        val dataSet = PieDataSet(entries, "").apply {
            // urutan warna = urutan fase: Pre-Nursery, Main-Nursery, Siap Tanam
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.status_info),
                ContextCompat.getColor(requireContext(), R.color.status_warning),
                ContextCompat.getColor(requireContext(), R.color.status_ok)
            )
            sliceSpace = 2f
            valueTextColor = Color.WHITE
            valueTextSize = 13f
        }

        // 3. Pasang data ke chart dan atur tampilannya
        val pieChart = binding.pieChartStok
        pieChart.data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart)) // tampilkan dalam persen
        }
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false) // nama fase cukup di legenda
        pieChart.centerText = "Total\n${DataBibit.formatAngka(DataBibit.totalStok())} bibit"
        pieChart.setCenterTextSize(14f)
        pieChart.holeRadius = 52f
        pieChart.transparentCircleRadius = 56f
        pieChart.legend.isWordWrapEnabled = true
        pieChart.animateY(800)
        pieChart.invalidate() // gambar ulang chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}