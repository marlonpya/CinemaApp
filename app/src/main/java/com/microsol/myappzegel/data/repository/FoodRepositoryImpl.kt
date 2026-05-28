package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.remote.MealDbApiService
import com.microsol.myappzegel.domain.model.Food
import com.microsol.myappzegel.domain.repository.FoodRepository

// Concrete implementation — fetches meals from TheMealDB and maps to domain model
class FoodRepositoryImpl(
    private val api: MealDbApiService
) : FoodRepository {

    override suspend fun getFoods(): List<Food> {
        val meals = api.getRandomMeal().meals ?: return emptyList()
        return meals.map { dto ->
            val numericId = dto.idMeal.toIntOrNull() ?: 0
            Food(
                id              = numericId,
                name            = dto.strMeal,
                description     = dto.strInstructions?.take(200)?.trimEnd()?.plus("...") ?: dto.strMeal,
                category        = dto.strCategory ?: dto.strArea ?: "Internacional",
                price           = (numericId % 20 + 10) * 0.9,
                rating          = ((numericId % 15) + 35) / 10.0f,
                imageUrl        = dto.strMealThumb ?: "",
                preparationTime = "${(numericId % 20) + 10} min",
                isAvailable     = true
            )
        }
    }

    override suspend fun getFoodById(id: Int): Food? {
        return try {
            val dto = api.getMealById(id.toString()).meals?.firstOrNull() ?: return null
            val numericId = dto.idMeal.toIntOrNull() ?: 0
            Food(
                id              = numericId,
                name            = dto.strMeal,
                description     = dto.strInstructions?.take(200)?.trimEnd()?.plus("...") ?: dto.strMeal,
                category        = dto.strCategory ?: dto.strArea ?: "Internacional",
                price           = (numericId % 20 + 10) * 0.9,
                rating          = ((numericId % 15) + 35) / 10.0f,
                imageUrl        = dto.strMealThumb ?: "",
                preparationTime = "${(numericId % 20) + 10} min",
                isAvailable     = true
            )
        } catch (e: Exception) {
            null
        }
    }
}
