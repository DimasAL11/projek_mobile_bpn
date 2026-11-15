package com.example.tif_dimas.stokbibit

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tif_dimas.R
import com.example.tif_dimas.data.DataBibit
import com.example.tif_dimas.data.StokBibit
import com.example.tif_dimas.databinding.ItemStokBibitBinding

/** Adapter RecyclerView: menghubungkan data StokBibit dengan tampilan item_stok_bibit.xml */
class StokBibitAdapter(
    private val stokList: List<StokBibit>,
    private val onItemClick: (StokBibit) -> Unit
) : RecyclerView.Adapter<StokBibitAdapter.StokViewHolder>() {

    inner class StokViewHolder(val binding: ItemStokBibitBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StokViewHolder {
        val binding = ItemStokBibitBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StokViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StokViewHolder, position: Int) {
        val item = stokList[position]
        with(holder.binding) {
            tvJenis.text = item.jenis
            tvFase.text = item.fase
            tvJumlah.text = "Jumlah: ${DataBibit.formatAngka(item.jumlah)} bibit"
            tvUmur.text = "Umur: ${item.umurBulan} bulan"
            tvLokasi.text = "Lokasi: ${item.lokasi}"

            // Warna badge mengikuti fase, sama seperti warna di pie chart Beranda
            val warnaFase = when (item.fase) {
                "Pre-Nursery" -> R.color.status_info
                "Main-Nursery" -> R.color.status_warning
                else -> R.color.status_ok
            }
            tvFase.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(root.context, warnaFase))

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = stokList.size
}
