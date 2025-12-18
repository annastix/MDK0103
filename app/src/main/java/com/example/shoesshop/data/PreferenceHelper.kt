package com.example.shoesshop.data

import android.content.Context

private const val PREF_NAME = "shoe_shop_prefs"
private const val KEY_RESET_EMAIL = "reset_email"
private const val KEY_RESET_OTP = "reset_otp"

object PreferenceHelper {

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

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
}
