package com.microsol.myappzegel.domain.repository

import com.microsol.myappzegel.domain.model.Food

// Repository contract for food data — separated from movie operations (Interface Segregation)
interface FoodRepository {
    suspend fun getFoods(): List<Food>
    suspend fun getFoodById(id: Int): Food?
}
