// data/service/FavouriteService.kt
package com.example.shoesshop.data.service

import com.example.shoesshop.data.models.FavouriteWithProductDto
import retrofit2.Response
import retrofit2.http.*

data class FavouriteRequest(
    val product_id: String,
    val user_id: String
)

interface FavouriteService {

    // товары в избранном (join favourite + products)
    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("favourite")
    suspend fun getFavouriteProducts(
        @Query("user_id") userIdEq: String,
        @Query("select") select: String =
            "products!favourite_product_id_fkey(*)"
    ): Response<List<FavouriteWithProductDto>>

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("favourite")
    suspend fun addToFavourite(
        @Body body: FavouriteRequest
    ): Response<Unit>

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @DELETE("favourite")
    suspend fun removeFromFavourite(
        @Query("product_id") productIdEq: String,
        @Query("user_id") userIdEq: String
    ): Response<Unit>
}
