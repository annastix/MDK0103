package com.example.shoesshop.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class CreateProfileRequest(
    val user_id: String,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null
)

interface ProfileService {

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("rest/v1/profiles")
    suspend fun createProfile(
        @Body body: CreateProfileRequest
    ): Response<Unit>
}
