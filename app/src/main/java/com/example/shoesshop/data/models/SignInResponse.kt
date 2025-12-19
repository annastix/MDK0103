package com.example.shoesshop.data.models

data class SignInResponse(
    val access_token: String?,
    val token_type: String?,
    val expires_in: Int?,
    val user: SupabaseUser2?
)

data class SupabaseUser2(
    val id: String?,
    val email: String?
)

