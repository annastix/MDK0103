package com.example.shoesshop.data.service

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

data class CartItemDto(
    val id: String,
    @SerializedName("count") val count: Int?,
    @SerializedName("product_id") val productId: String?,
    val products: ProductNestedDto?
)

data class ProductNestedDto(
    val title: String?,
    @SerializedName("cost") val cost: Double?
)

// тело PATCH-запроса для обновления количества
data class CartUpdateRequest(
    val count: Int
)

// тело POST-запроса для добавления товара в корзину
data class CartInsertRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("count") val count: Int = 1
)

interface CartService {

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Query("select")
        select: String = "id,count,product_id,products(title,cost)",
        @Query("user_id")
        userIdFilter: String // "eq.<user_id>"
    ): List<CartItemDto>

    @PATCH("cart")
    suspend fun updateCartItem(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Query("id") idFilter: String,      // "eq.<cartId>"
        @Body body: CartUpdateRequest
    ): Response<Unit>

    @DELETE("cart")
    suspend fun deleteCartItem(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Query("id") idFilter: String       // "eq.<cartId>"
    ): Response<Unit>

    @POST("cart")
    suspend fun addCartItem(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Body body: CartInsertRequest
    ): Response<Unit>

    @DELETE("cart")
    suspend fun deleteCartByUser(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Query("user_id") userIdFilter: String // "eq.<userId>"
    )

}
