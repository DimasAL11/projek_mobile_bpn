package com.example.tif_dimas.data

import com.example.tif_dimas.R
import java.util.Locale

/** Satu lokasi stok bibit (sama dengan kartu di menu Stok Bibit) */
data class StokBibit(
    val lokasi: String,
    val jenis: String,
    val fase: String,
    val jumlah: Int,
    val umurBulan: Int
)

/** Satu catatan bibit afkir (sama dengan kartu di menu Bibit Afkir) */
data class BibitAfkir(
    val penyebab: String,
    val labelGrafik: String, // label pendek untuk sumbu X grafik
    val jenis: String,
    val lokasi: String,
    val jumlah: Int,
    val tanggal: String
)

/** Satu jenis bibit (item ListView di menu Jenis Bibit) */
data class JenisBibit(
    val nama: String,
    val label: String,       // teks badge, contoh: "Unggul"
    val warnaLabel: Int,     // warna badge (resource color)
    val deskripsi: List<String>
)

/**
 * Sumber data untuk grafik. Angkanya sama dengan kartu di layout,
 * sehingga grafik dihitung dari data, bukan diketik manual.
 */
object DataBibit {

    val daftarStok = listOf(
        StokBibit("Bedengan A1", "Tenera (DxP)", "Pre-Nursery", 1200, 2),
        StokBibit("Blok B2", "Tenera (DxP)", "Main-Nursery", 850, 8),
        StokBibit("Blok B3", "Tenera (DxP)", "Siap Tanam", 600, 12),
        StokBibit("Blok C1", "Dura", "Main-Nursery", 150, 6)
    )

    val daftarAfkir = listOf(
        BibitAfkir("Bibit Kerdil", "Kerdil", "Tenera (DxP)", "Blok B2", 45, "14 Okt 2026"),
        BibitAfkir("Daun Abnormal", "Abnormal", "Tenera (DxP)", "Bedengan A1", 30, "10 Okt 2026"),
        BibitAfkir("Terserang Hama", "Hama", "Tenera (DxP)", "Blok B3", 18, "07 Okt 2026"),
        BibitAfkir("Busuk Pangkal Batang", "Busuk", "Dura", "Blok C1", 7, "02 Okt 2026")
    )

    val daftarJenis = listOf(
        JenisBibit(
            "Tenera (DxP)", "Unggul", R.color.status_ok,
            listOf(
                "Hasil persilangan Dura x Pisifera.",
                "Cangkang tipis, rendemen minyak tinggi.",
                "Dipakai untuk bibit komersial."
            )
        ),
        JenisBibit(
            "Dura", "Induk Betina", R.color.status_info,
            listOf(
                "Cangkang tebal, daging buah tipis.",
                "Dipakai sebagai induk betina persilangan."
            )
        ),
        JenisBibit(
            "Pisifera", "Induk Jantan", R.color.status_warning,
            listOf(
                "Buah tanpa cangkang, umumnya steril betina.",
                "Dipakai sebagai sumber serbuk sari."
            )
        )
    )

    fun totalStok(): Int = daftarStok.sumOf { it.jumlah }

    fun totalAfkir(): Int = daftarAfkir.sumOf { it.jumlah }

    /** Jumlah stok dikelompokkan per fase, contoh: {Pre-Nursery=1200, Main-Nursery=1000, Siap Tanam=600} */
    fun stokPerFase(): Map<String, Int> =
        daftarStok.groupBy { it.fase }.mapValues { (_, list) -> list.sumOf { it.jumlah } }

    /** Format angka gaya Indonesia: 2800 -> "2.800" */
    fun formatAngka(angka: Int): String =
        String.format(Locale.forLanguageTag("id-ID"), "%,d", angka)
}