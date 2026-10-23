package com.example.tif_dimas.auth

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Menampilkan pesan error dengan MaterialAlertDialog.
 * Dipakai di halaman Login, input Gmail dan Registrasi.
 */
fun Context.showErrorDialog(title: String, message: String) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .show()
}