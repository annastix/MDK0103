package com.example.shoeshop.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.shoeshop.data.models.VerifyOTPRequest
import kotlinx.coroutines.launch

class VerificationViewModel : ViewModel() {


    private fun getEmailFromSharedPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_email", "") ?: ""
    }

    private fun saveAuthToken(context: Context, token: String?) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
        Log.d("SendOTPViewModel", "Auth token saved")
    }

    fun getStoredEmail(context: Context): String {
        return getEmailFromSharedPreferences(context)
    }

    private fun markUserAsVerified(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_user_verified", true)
        editor.apply()
        Log.d("SendOTPViewModel", "User marked as verified")
    }

    fun verifyOTP(
        token: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (token.isEmpty()) {
            onError("Please enter OTP token")
            return
        }

        val userEmail = getEmailFromSharedPreferences(context)
        if (userEmail.isEmpty()) {
            onError("User email not found. Please restart registration.")
            return
        }




        viewModelScope.launch {
//            try {
//                val verifyOtpRequest = VerifyOTPRequest(
//                    email = userEmail,
//                    token = token,
//                    type = "signup"
//                )

//                val response = RetrofitInstance.userManagementService.verifyOtp(verifyOtpRequest)
//
//                if (response.isSuccessful) {
//                    response.body()?.let { verifyResponse ->
//                        Log.v("SendOTPViewModel", "OTP verified successfully")
//                        Log.d("SendOTPViewModel", "Access token: ${verifyResponse.accessToken}")
//
//                        saveAuthToken(context, verifyResponse.accessToken)
//                        markUserAsVerified(context)
//                        onSuccess()
//                    }
//                } else {
//                    val errorMessage = when (response.code()) {
//                        401 -> "Invalid OTP token"
//                        400 -> "Invalid request or expired OTP"
//                        404 -> "User not found"
//                        else -> "OTP verification failed: ${response.message()}"
//                    }
//                    onError(errorMessage)
//                }
//            } catch (e: Exception) {
//                Log.e("SendOTPViewModel", "OTP verification error", e)
//                onError("Network error: ${e.message}")
////            }
//        }
//    }
        }
    }
}