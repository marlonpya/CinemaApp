package com.microsol.myappzegel.data.remote

import com.microsol.myappzegel.data.remote.dto.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApiService {

    @GET("random.php")
    suspend fun getRandomMeal(): MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealListResponse

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") name: String
    ): MealListResponse

    @GET("categories.php")
    suspend fun getCategories(): MealListResponse
}
