package com.microsol.myappzegel.data.mapper

import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId
import com.microsol.myappzegel.data.remote.dto.MealDto

// ─── TheMealDB meal → domain Food ────────────────────────────────────────────

fun MealDto.toDomain(): Food {
    val numericId = idMeal.toIntOrNull() ?: 0

    // Deterministic price derived from ID so the same meal always shows the same price
    val price = (numericId % 20 + 10) * 0.9

    // Deterministic rating (3.5–4.9) derived from ID
    val rating = ((numericId % 15) + 35) / 10.0f

    // Deterministic preparation time (10–29 min) derived from ID
    val prepTime = "${(numericId % 20) + 10} min"

    // Build description from instructions; truncate if too long
    val description = when {
        !strInstructions.isNullOrBlank() -> strInstructions.take(200).trimEnd() + "..."
        else -> ingredientsSummary().ifBlank { strMeal }
    }

    return Food(
        id              = FoodId(numericId),
        name            = strMeal,
        description     = description,
        category        = strCategory ?: strArea ?: "Internacional",
        price           = price,
        rating          = rating,
        imageUrl        = strMealThumb ?: "",
        preparationTime = prepTime,
        isAvailable     = true
    )
}
