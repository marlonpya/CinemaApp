package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.mapper.toDomain
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId
import com.microsol.myappzegel.data.remote.MealDbApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class FoodRepositoryImpl(
    private val api: MealDbApiService
) : FoodRepository {

    override suspend fun getFoods(): List<Food> = coroutineScope {
        // Fire 10 random-meal requests in parallel; deduplicate by ID afterwards
        val deferred = List(10) { async { runCatching { api.getRandomMeal().meals } } }

        deferred
            .mapNotNull { it.await().getOrNull() }
            .flatten()
            .mapNotNull { it }
            .distinctBy { it.idMeal }
            .map { it.toDomain() }
    }

    override suspend fun getFoodById(id: FoodId): Food? {
        return runCatching {
            api.getMealById(id.value.toString())
                .meals
                ?.firstOrNull()
                ?.toDomain()
        }.getOrNull()
    }
}
