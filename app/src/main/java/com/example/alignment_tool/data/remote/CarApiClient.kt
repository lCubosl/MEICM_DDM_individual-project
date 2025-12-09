package com.example.alignment_tool.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CarApiClient {

    private const val BASE_URL = "https://carapi.app/api/"

    fun createService(apiKey: String, apiSecret: String): CarApiService {

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", apiKey)
                .addHeader("x-api-secret", apiSecret)
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CarApiService::class.java)
    }
}
