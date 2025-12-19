package com.example.shoesshop.data.service

import com.example.shoesshop.data.models.Categories
import retrofit2.http.GET
import retrofit2.http.Query

interface DatabaseService {

    // SELECT * FROM public.categories
    @GET("categories")
    suspend fun getCategories(
        @Query("select") select: String = "*"
    ): List<Categories>
}
