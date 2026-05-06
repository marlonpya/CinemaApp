package com.microsol.myappzegel.domain.usecase

import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId
import com.microsol.myappzegel.data.repository.FoodRepository

// ─────────────────────────────────────────────────────────────────────────────
// Use case for food data — same pattern as GetMoviesUseCase.
//
// ARCHITECTURE NOTE:
// Domain layer classes (use cases) ONLY import from the data layer models and
// repository interfaces. They never import from `android.*` or the presentation
// layer. This makes the domain layer portable and unit-testable without a
// running Android device.
// ─────────────────────────────────────────────────────────────────────────────
class GetFoodsUseCase(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(): List<Food> = repository.getFoods()

    suspend operator fun invoke(id: FoodId): Food? = repository.getFoodById(id)
}
