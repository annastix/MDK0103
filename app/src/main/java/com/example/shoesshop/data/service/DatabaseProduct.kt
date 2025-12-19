package com.example.shoesshop.data.service

import com.example.shoesshop.data.models.ProductDto
import com.example.shoesshop.data.models.Products
import retrofit2.http.GET
import retrofit2.http.Query

interface DatabaseProduct {

    @GET("products")
    suspend fun getProducts(
        @Query("select") select: String = "*"
    ): List<ProductDto>

    @GET("products")
    suspend fun getThreeProducts(
        @Query("select") select: String = "*",
        @Query("limit") limit: Int = 3
    ): List<ProductDto>

    @GET("products")
    suspend fun getBestSellers(
        @Query("is_best_seller") filter: String = "eq.true",
        @Query("select") select: String = "*"
    ): List<ProductDto>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category_id") categoryFilter: String, // "eq.<uuid>"
        @Query("select") select: String = "*"
    ): List<ProductDto>

    @GET("products")
    suspend fun getProductById(
        @Query("id") idFilter: String,      // "eq.<id>"
        @Query("select") select: String = "*"
    ): List<ProductDto>

}

object CategoryNames {
    val map = mapOf(
        "4f3a690b-41bf-4fca-8ffc-67cc385c6637" to "Tennis",
        "76ab9d74-7d5b-4dee-9c67-6ed4019fa202" to "Men",
        "8143b506-d70a-41ec-a5eb-3cf09627da9e" to "Women",
        "ea4ed603-8cbe-4d57-a359-b6b843a645bc" to "Outdoor"
    )

    fun nameFor(id: String?): String = id?.let { map[it] ?: it } ?: ""
}