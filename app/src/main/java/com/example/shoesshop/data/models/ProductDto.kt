package com.example.shoesshop.data.models

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("cost") val cost: Double,
    @SerializedName("description") val description: String,
    @SerializedName("is_best_seller") val isBestSeller: Boolean
)
