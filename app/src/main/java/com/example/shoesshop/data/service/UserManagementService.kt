package com.example.shoesshop.data.service

import com.example.shoesshop.data.models.ForgotPasswordRequest
import com.example.shoesshop.data.models.RegisterRequest
import com.example.shoesshop.data.models.SignInRequest
import com.example.shoesshop.data.models.VerifyOTPRequest
import com.example.shoesshop.data.models.VerifyOTPResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

const val API_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt0ZGpuc3V0Y2luem9rcGhxZ2p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTcyMzAsImV4cCI6MjA4MTMzMzIzMH0.S-3087j4Bp0KLKDC7NPeiEPF2wX2Hayp6t50-ngkvjc"

interface UserManagementService {

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: RegisterRequest): Response<RegisterRequest>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInRequest>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOTPRequest): Response<VerifyOTPResponse>

    // Для повторной отправки OTP
    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/otp")
    suspend fun resendOtp(@Body otpRequest: ResendOTPRequest): Response<Unit>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/recover")
    suspend fun recoverPassword(@Body body: ForgotPasswordRequest): Response<Unit>
}

data class ResendOTPRequest(
    val email: String,
    val type: String = "signup"
)