package com.example.shoesshop.data.models

data class Favourite(
    val id: String?,        // uuid записи в favourite
    val product_id: String,
    val user_id: String
)

data class FavouriteWithProduct(
    val products: Products
)