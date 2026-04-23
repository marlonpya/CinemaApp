package com.microsol.myappzegel.data.model

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: @JvmInline value class
// Same pattern as MovieId — keeps type-safety between domain concepts.
// ─────────────────────────────────────────────────────────────────────────────
@JvmInline
value class FoodId(val value: Int)

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: data class with a custom getter
//
// Kotlin properties can have a custom `get()` block. Here `formattedPrice` is
// a computed property (no backing field) derived from `price` at read time.
// This avoids storing redundant data and keeps formatting in the model layer.
// ─────────────────────────────────────────────────────────────────────────────
data class Food(
    val id: FoodId,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,         // raw numeric price stored in the model
    val rating: Float,
    val imageUrl: String,
    val preparationTime: String,   // e.g. "20 min"
    val isAvailable: Boolean = true
) {
    // KOTLIN CONCEPT: Custom getter
    // `formattedPrice` is a read-only property whose value is computed each
    // time it is accessed. No setter is needed (or possible) because it has
    // no backing field.
    val formattedPrice: String
        get() = "S/. %.2f".format(price)

    // KOTLIN CONCEPT: Custom getter with logic
    // Derived property — another example of a computed val.
    val availabilityLabel: String
        get() = if (isAvailable) "Disponible" else "Agotado"
}
