package com.example.shoesshop.data.models

data class ProfileInsertRequest(
    val user_id: String,
    val firstname: String?,
    val lastname: String?,
    val address: String?,
    val phone: String?
)