package com.dataxing.indiapolls.network

import com.dataxing.indiapolls.baseUrl
import com.dataxing.indiapolls.data.UserPreferencesRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    private fun getInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("language", UserPreferencesRepository.currentLanguage)
                    .addHeader("app_type", "mobile")
                val request = requestBuilder.build()
                chain.proceed(request)
            })
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getInstance().create(ApiService::class.java)
}