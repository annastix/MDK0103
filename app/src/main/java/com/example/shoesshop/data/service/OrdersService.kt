package com.example.shoesshop.data.service

import com.example.shoesshop.data.models.OrderHistoryDto
import com.example.shoesshop.data.models.OrderItemRequest
import com.example.shoesshop.data.models.OrderRequest
import com.example.shoesshop.data.models.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface OrdersService {

    // создаём заказ, тело ответа игнорируем
    @POST("orders")
    suspend fun createOrder(
        @Body body: OrderRequest
    )

    // берём последний заказ пользователя (по created_at)
    @GET("orders?select=id&order=created_at.desc&limit=1")
    suspend fun getLastOrder(
        @Query("user_id") userIdFilter: String // передаём "eq.<user_id>"
    ): List<OrderResponse>

    @POST("orders_items")
    suspend fun addItems(
        @Body items: List<OrderItemRequest>
    )

    // История заказов текущего пользователя
    @GET("orders")
    suspend fun getOrders(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Query("user_id") userIdFilter: String,
        @Query("select") select: String =
            "id,created_at,delivery_coast,status_id,orders_items(title,coast,count,product_id)",
        @Query("order") order: String = "created_at.desc"
    ): List<OrderHistoryDto>

}
