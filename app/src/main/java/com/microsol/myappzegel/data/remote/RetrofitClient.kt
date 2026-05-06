package com.microsol.myappzegel.data.remote

import com.microsol.myappzegel.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val TMDB_BASE_URL  = "https://api.themoviedb.org/3/"
    private const val MEAL_DB_BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    // Shared OkHttp client for TMDb — adds Bearer auth + accept headers
    private val tmdbHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_READ_ACCESS_TOKEN}")
                    .addHeader("accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }
            }
            .build()
    }

    // Shared OkHttp client for TheMealDB (no auth required)
    private val mealDbHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BASIC
                        }
                    )
                }
            }
            .build()
    }

    val tmdbApi: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(tmdbHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }

    val mealDbApi: MealDbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MEAL_DB_BASE_URL)
            .client(mealDbHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealDbApiService::class.java)
    }
}
