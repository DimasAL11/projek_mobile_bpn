package com.example.tif_dimas.Profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tif_dimas.auth.AuthActivity
import com.example.tif_dimas.databinding.FragmentProfilBinding
import com.example.tif_dimas.auth.BiometricHelper

class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    // Fitur baru: login sidik jari
    private lateinit var biometricHelper: BiometricHelper
    private var username = ""
    // true saat saklar diubah oleh kode (bukan oleh user), supaya listener tidak ikut jalan
    private var sedangUpdateSaklar = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment harus memakai (requireActivity() as AppCompatActivity) untuk Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Profil"
        }

        // requireContext() karena sedang berada di Fragment
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // Ambil username yang disimpan saat login
        username = sharedPref.getString("username", "Guest").orEmpty()
        binding.tvLoginAs.text = "Login sebagai: $username"

        // Fitur baru: saklar login sidik jari
        biometricHelper = BiometricHelper(requireActivity())
        setupSaklarSidikJari()

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah kamu yakin ingin keluar?")
                .setPositiveButton("Ya") { dialog, _ ->
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    dialog.dismiss()

                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    // destroy BaseActivity karena posisi kita ada di fragment
                    requireActivity().finish()
                }
                .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
    /** Saklar aktif/nonaktif login sidik jari untuk akun yang sedang login */
    private fun setupSaklarSidikJari() {
        // HP tidak mendukung -> saklar dimatikan dan tampilkan alasannya
        if (!biometricHelper.isAvailable()) {
            setSaklar(false)
            binding.switchBiometric.isEnabled = false
            binding.tvBiometricInfo.text = biometricHelper.statusMessage()
            return
        }

        setSaklar(biometricHelper.isEnabledFor(username))
        updateInfoSidikJari()

        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (sedangUpdateSaklar) return@setOnCheckedChangeListener

            if (isChecked) {
                // Mengaktifkan: wajib verifikasi sidik jari dulu
                biometricHelper.authenticate(
                    title = "Aktifkan Login Sidik Jari",
                    subtitle = "Konfirmasi untuk akun $username",
                    onSuccess = {
                        biometricHelper.enable(username)
                        if (_binding != null) updateInfoSidikJari()
                    },
                    onError = { if (_binding != null) setSaklar(false) },
                    onCancel = { if (_binding != null) setSaklar(false) }
                )
            } else {
                // Menonaktifkan: langsung, tanpa verifikasi
                biometricHelper.disable()
                updateInfoSidikJari()
            }
        }
    }

    private fun setSaklar(aktif: Boolean) {
        sedangUpdateSaklar = true
        binding.switchBiometric.isChecked = aktif
        sedangUpdateSaklar = false
    }

    private fun updateInfoSidikJari() {
        binding.tvBiometricInfo.text = if (biometricHelper.isEnabledFor(username)) {
            "Aktif untuk akun $username"
        } else {
            "Masuk tanpa mengetik password"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}