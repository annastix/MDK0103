package com.example.shoesshop.data.models

// История заказов + позиции
data class OrderHistoryDto(
    val id: Long,
    val created_at: String?,
    val delivery_coast: Long?,
    val status_id: String?,
    val orders_items: List<OrderItemRequest>? = null
)
