package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: Interface
// SOLID PRINCIPLE: Interface Segregation (I)
//
// Food-related data operations are separated from movie operations.
// Each interface stays focused on a single responsibility.
// ─────────────────────────────────────────────────────────────────────────────
interface FoodRepository {
    fun getFoods(): List<Food>
    fun getFoodById(id: FoodId): Food?
}
