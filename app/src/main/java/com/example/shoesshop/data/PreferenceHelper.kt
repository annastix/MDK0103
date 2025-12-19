package com.example.shoesshop.data

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

private const val PREF_NAME = "shoe_shop_prefs"
private const val KEY_RESET_EMAIL = "reset_email"
private const val KEY_RESET_OTP = "reset_otp"

// Ключи пользователя
private const val KEY_USER_ID = "user_id"
private const val KEY_USER_EMAIL = "user_email"
private const val KEY_IS_LOGGED_IN = "is_logged_in"
private const val KEY_LOGIN_TIME = "login_time"

private const val SECURE_PREF_NAME = "secure_user_prefs"
object PreferenceHelper {

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ---------- reset password ----------
    fun saveResetEmail(context: Context, email: String) {
        prefs(context).edit().putString(KEY_RESET_EMAIL, email).apply()
    }

    fun getResetEmail(context: Context): String? =
        prefs(context).getString(KEY_RESET_EMAIL, null)

    fun clearResetEmail(context: Context) {
        prefs(context).edit().remove(KEY_RESET_EMAIL).apply()
    }

    fun saveResetOtp(context: Context, otp: String) {
        prefs(context).edit().putString(KEY_RESET_OTP, otp).apply()
    }

    fun getResetOtp(context: Context): String? =
        prefs(context).getString(KEY_RESET_OTP, null)

    fun clearResetOtp(context: Context) {
        prefs(context).edit().remove(KEY_RESET_OTP).apply()
    }

    // ---------- user ----------
    fun saveUser(
        context: Context,
        userId: String,
        email: String
    ) {
        prefs(context).edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_EMAIL, email)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putLong(KEY_LOGIN_TIME, System.currentTimeMillis())
            .apply()
        Log.d("PreferenceHelper", "User saved: id=$userId, email=$email")
    }

    private fun securePrefs(context: Context): android.content.SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            SECURE_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getUserId(context: Context): String? =
        securePrefs(context).getString(KEY_USER_ID, null)

    fun getUserEmail(context: Context): String? =
        securePrefs(context).getString("user_email", null)

    fun isLoggedIn(context: Context): Boolean =
        securePrefs(context).getBoolean("is_logged_in", false)

    fun logout(context: Context) {
        securePrefs(context).edit().clear().apply()
    }

    fun getLoginTime(context: Context): Long =
        prefs(context).getLong(KEY_LOGIN_TIME, 0L)
}
