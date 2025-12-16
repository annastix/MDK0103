package com.example.shoesshop.data.models

data class VerifyOTPRequest (
    val email: String,
    val token: String,
    val type: String = "email"
)