package com.example.tif_dimas.JenisBibit

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.tif_dimas.data.JenisBibit
import com.example.tif_dimas.databinding.ItemJenisBibitBinding

/** CustomAdapter (turunan BaseAdapter) untuk ListView di menu Jenis Bibit */
class JenisBibitAdapter(
    private val context: Context,
    private val jenisList: List<JenisBibit>
) : BaseAdapter() {

    override fun getCount(): Int = jenisList.size

    override fun getItem(position: Int): JenisBibit = jenisList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // convertView != null -> pakai ulang view lama, binding disimpan di tag
        val binding = if (convertView == null) {
            ItemJenisBibitBinding.inflate(LayoutInflater.from(context), parent, false).also {
                it.root.tag = it
            }
        } else {
            convertView.tag as ItemJenisBibitBinding
        }

        val item = getItem(position)
        binding.tvNama.text = item.nama
        binding.tvLabel.text = item.label
        binding.tvLabel.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, item.warnaLabel))
        binding.tvDeskripsi.text = item.deskripsi.joinToString("\n")

        return binding.root
    }
}
