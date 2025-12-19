package com.example.shoesshop.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

data class CreateProfileRequest(
    val user_id: String,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null
)

data class Profile(
    val id: String?,        // uuid профиля
    val user_id: String,    // uuid из auth.users
    val photo: String?,
    val firstname: String?,
    val lastname: String?,
    val address: String?,
    val phone: String?
)

interface ProfileService {
    // получить профиль по user_id
    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("profiles")
    suspend fun getProfile(
        @Query("user_id") userIdEq: String // формат: "eq.<uuid>"
    ): Response<List<Profile>>

    // создать профиль
    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("profiles")
    suspend fun createProfile(
        @Body body: CreateProfileRequest
    ): Response<List<Profile>>

    // обновить профиль по id
    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("profiles")
    suspend fun updateProfile(
        @Query("id") profileIdEq: String, // "eq.<uuid>"
        @Body body: CreateProfileRequest
    ): Response<List<Profile>>
}


