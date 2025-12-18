package com.example.shoesshop.data

import com.example.shoesshop.data.service.API_KEY
import com.example.shoesshop.data.service.DatabaseProduct
import com.example.shoesshop.data.service.DatabaseService
import com.example.shoesshop.data.service.ProfileService
import com.example.shoesshop.data.service.UserManagementService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    const val SUBABASE_URL = "https://ktdjnsutcinzokphqgjv.supabase.co"
    const val REST_URL = "$SUBABASE_URL/rest/v1/"

    private const val PROXY_HOST = "10.207.106.71"
    private const val PROXY_PORT = 3128
    private const val USE_PROXY = false

    var client: OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (USE_PROXY) {
                proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(PROXY_HOST, PROXY_PORT)))
            }
        }
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer $API_KEY")
                .header("Content-Type", "application/json")
                .method(original.method, original.body)

            val url = original.url.toString()
            if (url.contains("/auth/")) {
                requestBuilder.removeHeader("Authorization")
                requestBuilder.header("apikey", API_KEY)
            }

            chain.proceed(requestBuilder.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitAuth: Retrofit = Retrofit.Builder()
        .baseUrl(SUBABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SUBABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val retrofitRest: Retrofit = Retrofit.Builder()
        .baseUrl(REST_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userManagementService: UserManagementService =
        retrofitAuth.create(UserManagementService::class.java)

    val databaseService: DatabaseService =
        retrofitRest.create(DatabaseService::class.java)

    val productsService: DatabaseProduct =
        retrofitRest.create(DatabaseProduct::class.java)

    val profileService: ProfileService =
        retrofitRest.create(ProfileService::class.java)
}
