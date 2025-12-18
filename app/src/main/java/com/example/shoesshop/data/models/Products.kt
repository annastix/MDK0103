package com.example.shoesshop.data.models

import com.google.gson.annotations.SerializedName

data class Products(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String,
    val category: String,
    val imageUrl: String = "",
    val imageResId: Int
)