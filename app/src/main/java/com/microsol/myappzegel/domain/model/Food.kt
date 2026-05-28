package com.microsol.myappzegel.domain.model

// Domain model for a food item
data class Food(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    val rating: Float,
    val imageUrl: String,
    val preparationTime: String,
    val isAvailable: Boolean = true
) {
    // Computed property — derived from price, no backing field needed
    val formattedPrice: String
        get() = "S/. %.2f".format(price)

    // Computed property — derived from isAvailable
    val availabilityLabel: String
        get() = if (isAvailable) "Disponible" else "Agotado"
}
