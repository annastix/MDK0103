package com.example.shoesshop.data.models

import com.google.gson.annotations.SerializedName

data class VerifyOTPResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    val user: SupabaseUser?,
    val session: SupabaseSession?
)

data class SupabaseUser(
    val id: String?
)

data class SupabaseSession(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?
)
