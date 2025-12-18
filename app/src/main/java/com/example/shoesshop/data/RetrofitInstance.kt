package com.example.shoesshop.data

import com.example.shoesshop.data.service.DatabaseService
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

    private const val USE_PROXY = true

    var client: OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (USE_PROXY) {
                proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(PROXY_HOST, PROXY_PORT)))
            }
        }
        .addInterceptor { chain ->
            val original = chain.request()

            // Добавляем обязательные заголовки для Supabase REST API
            val requestBuilder = original.newBuilder()
                .header("apikey","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt0ZGpuc3V0Y2luem9rcGhxZ2p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTcyMzAsImV4cCI6MjA4MTMzMzIzMH0.S-3087j4Bp0KLKDC7NPeiEPF2wX2Hayp6t50-ngkvjc")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt0ZGpuc3V0Y2luem9rcGhxZ2p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTcyMzAsImV4cCI6MjA4MTMzMzIzMH0.S-3087j4Bp0KLKDC7NPeiEPF2wX2Hayp6t50-ngkvjc")
                .header("Content-Type", "application/json")
                .method(original.method, original.body)

            // Для авторизации могут быть другие заголовки
            val url = original.url.toString()
            if (url.contains("/auth/")) {
                // Для auth endpoints используем только apikey
                requestBuilder.removeHeader("Authorization")
                requestBuilder.header("apikey","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt0ZGpuc3V0Y2luem9rcGhxZ2p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTcyMzAsImV4cCI6MjA4MTMzMzIzMH0.S-3087j4Bp0KLKDC7NPeiEPF2wX2Hayp6t50-ngkvjc")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitAuth = Retrofit.Builder()
        .baseUrl(SUBABASE_URL) // Базовый URL для auth
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SUBABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val retrofitRest = Retrofit.Builder()
        .baseUrl(REST_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userManagementService = retrofit.create(UserManagementService::class.java)

    val databaseService = retrofitRest.create(DatabaseService::class.java)
}