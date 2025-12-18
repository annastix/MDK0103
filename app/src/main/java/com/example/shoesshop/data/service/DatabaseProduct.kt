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
    suspend fun getBestSellers(
        @Query("is_best_seller") filter: String = "eq.true",
        @Query("select") select: String = "*"
    ): List<ProductDto>
}
