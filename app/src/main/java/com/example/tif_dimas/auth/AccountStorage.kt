package com.example.tif_dimas.auth

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Satu akun hasil registrasi */
data class Account(
    val nama: String,
    val email: String,
    val username: String,
    val password: String
)
class AccountStorage(context: Context) {

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /** Ambil semua akun yang tersimpan */
    fun getAccounts(): List<Account> {
        val array = JSONArray(pref.getString(KEY_ACCOUNTS, "[]"))
        val accounts = mutableListOf<Account>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            accounts.add(
                Account(
                    nama = obj.getString("nama"),
                    email = obj.getString("email"),
                    username = obj.getString("username"),
                    password = obj.getString("password")
                )
            )
        }
        return accounts
    }

    /** Tambahkan satu akun baru ke daftar lalu simpan */
    fun addAccount(account: Account) {
        val array = JSONArray(pref.getString(KEY_ACCOUNTS, "[]"))
        val obj = JSONObject()
            .put("nama", account.nama)
            .put("email", account.email)
            .put("username", account.username)
            .put("password", account.password)
        array.put(obj)
        pref.edit().putString(KEY_ACCOUNTS, array.toString()).apply()
    }

    fun isUsernameTaken(username: String): Boolean =
        getAccounts().any { it.username.equals(username, ignoreCase = true) }

    fun isEmailTaken(email: String): Boolean =
        getAccounts().any { it.email.equals(email, ignoreCase = true) }

    /** Dipakai saat login: null jika tidak ada akun yang cocok */
    fun findAccount(username: String, password: String): Account? =
        getAccounts().find { it.username == username && it.password == password }

    fun countAccounts(): Int = getAccounts().size

    companion object {
        const val PREF_NAME = "register_pref"
        const val KEY_ACCOUNTS = "accounts"
    }
}