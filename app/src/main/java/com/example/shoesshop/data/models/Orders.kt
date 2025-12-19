package com.example.shoesshop.data.models

data class OrderRequest(
    val email: String?,
    val phone: String?,
    val address: String?,
    val user_id: String?,       // как в CheckoutViewModel
    val payment_id: String?,
    val delivery_coast: Long,
    val status_id: String
)

data class OrderResponse(
    val id: Long
)

data class OrderItemRequest(
    val title: String,
    val coast: Double,
    val count: Long,
    val order_id: Long,         // как в CheckoutViewModel
    val product_id: String
)
