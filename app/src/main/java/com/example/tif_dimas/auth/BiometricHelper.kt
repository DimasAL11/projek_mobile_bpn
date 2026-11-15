package com.example.tif_dimas.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Fitur baru: login dengan sidik jari (AndroidX Biometric).
 *
 * - cek apakah HP bisa memakai sidik jari (BiometricManager)
 * - tampilkan dialog sidik jari dari sistem (BiometricPrompt)
 * - simpan username yang mengaktifkan login sidik jari di SharedPreferences
 *   "biometric_pref" (terpisah dari user_pref, jadi tidak terhapus saat logout)
 *
 * Buat objek ini di onCreate (Activity) atau onViewCreated (Fragment),
 * karena BiometricPrompt sebaiknya dibuat sebelum dipakai, bukan saat tombol diklik.
 */
class BiometricHelper(private val activity: FragmentActivity) {

    private val pref = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Aksi setelah prompt selesai, diisi ulang setiap kali authenticate() dipanggil
    private var onSuccess: () -> Unit = {}
    private var onError: (String) -> Unit = {}
    private var onCancel: () -> Unit = {}

    private val biometricPrompt = BiometricPrompt(
        activity,
        ContextCompat.getMainExecutor(activity),
        object : BiometricPrompt.AuthenticationCallback() {
            // Sidik jari cocok
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }

            // Prompt ditutup: dibatalkan user, atau error (terlalu banyak percobaan, sensor error, ...)
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                    errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                    errorCode == BiometricPrompt.ERROR_CANCELED
                ) {
                    onCancel()
                } else {
                    onError(errString.toString())
                }
            }

            // onAuthenticationFailed(): jari tidak cocok. Prompt tetap terbuka dan sistem
            // sudah menampilkan pesan "tidak dikenali", jadi tidak perlu ditangani di sini.
        }
    )

    /** Status perangkat: BIOMETRIC_SUCCESS jika sidik jari bisa dipakai */
    fun checkStatus(): Int = BiometricManager.from(activity).canAuthenticate(BIOMETRIC_STRONG)

    fun isAvailable(): Boolean = checkStatus() == BiometricManager.BIOMETRIC_SUCCESS

    /** Pesan yang mudah dipahami untuk setiap status perangkat */
    fun statusMessage(): String = when (checkStatus()) {
        BiometricManager.BIOMETRIC_SUCCESS -> "Sidik jari siap digunakan."
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "HP ini tidak memiliki sensor sidik jari."
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Sensor sidik jari sedang tidak bisa digunakan."
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
            "Belum ada sidik jari terdaftar. Daftarkan dulu di Pengaturan HP > Keamanan."
        else -> "Sidik jari tidak dapat digunakan di HP ini."
    }

    // ---------- status aktivasi (biometric_pref) ----------

    fun getSavedUsername(): String? = pref.getString(KEY_USERNAME, null)

    fun isEnabledFor(username: String): Boolean = getSavedUsername() == username

    fun enable(username: String) {
        pref.edit().putString(KEY_USERNAME, username).apply()
    }

    fun disable() {
        pref.edit().remove(KEY_USERNAME).apply()
    }

    // ---------- tampilkan dialog sidik jari ----------

    fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {},
        onCancel: () -> Unit = {}
    ) {
        this.onSuccess = onSuccess
        this.onError = onError
        this.onCancel = onCancel

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Batal")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        const val PREF_NAME = "biometric_pref"
        const val KEY_USERNAME = "username"
    }
}