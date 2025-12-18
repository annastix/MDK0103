package com.example.shoesshop.data.models

import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val name: String,
    val isSelected: Boolean = false
)