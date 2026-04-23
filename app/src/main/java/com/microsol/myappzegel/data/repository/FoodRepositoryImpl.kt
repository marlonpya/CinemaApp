package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: Class with constructor + interface implementation
//
// `FoodRepositoryImpl` implements `FoodRepository`. The `override` keyword
// makes it explicit that these functions fulfill the interface contract.
// ─────────────────────────────────────────────────────────────────────────────
class FoodRepositoryImpl : FoodRepository {

    private val foods: List<Food> = buildFoodList()

    override fun getFoods(): List<Food> = foods

    override fun getFoodById(id: FoodId): Food? {
        return foods.find { it.id == id }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Hardcoded food catalogue — 8 popular dishes
    // ─────────────────────────────────────────────────────────────────────────
    private fun buildFoodList(): List<Food> = listOf(
        Food(
            id = FoodId(1),
            name = "Hamburguesa Clásica",
            description = "Jugosa hamburguesa de res con lechuga, tomate, queso cheddar y salsa especial de la casa en pan brioche tostado.",
            category = "Hamburguesas",
            price = 18.90,
            rating = 4.7f,
            imageUrl = "food_burger",
            preparationTime = "15 min"
        ),
        Food(
            id = FoodId(2),
            name = "Pizza Margherita",
            description = "Pizza italiana clásica con salsa de tomate San Marzano, mozzarella fresca, albahaca y un chorrito de aceite de oliva virgen extra.",
            category = "Pizzas",
            price = 24.50,
            rating = 4.6f,
            imageUrl = "food_pizza",
            preparationTime = "20 min"
        ),
        Food(
            id = FoodId(3),
            name = "Pollo a la Brasa",
            description = "Pollo a la brasa marinado con especias peruanas, acompañado de papas fritas crujientes y ensalada fresca con ají especial.",
            category = "Pollos",
            price = 32.00,
            rating = 4.8f,
            imageUrl = "food_chicken",
            preparationTime = "25 min"
        ),
        Food(
            id = FoodId(4),
            name = "Ceviche Clásico",
            description = "Fresco ceviche de pescado marinado en limón, ají limo, cebolla morada y cilantro, servido con choclo y cancha serrana.",
            category = "Mariscos",
            price = 28.00,
            rating = 4.9f,
            imageUrl = "food_ceviche",
            preparationTime = "10 min"
        ),
        Food(
            id = FoodId(5),
            name = "Pasta Carbonara",
            description = "Pasta spaghetti con salsa cremosa de huevo y queso parmesano, panceta crujiente y pimienta negra recién molida.",
            category = "Pastas",
            price = 22.00,
            rating = 4.5f,
            imageUrl = "food_pasta",
            preparationTime = "18 min"
        ),
        Food(
            id = FoodId(6),
            name = "Tacos al Pastor",
            description = "Tres tacos de cerdo marinado al pastor con piña, cebolla, cilantro y salsas verde y roja en tortillas de maíz hechas a mano.",
            category = "Mexicana",
            price = 19.50,
            rating = 4.6f,
            imageUrl = "food_tacos",
            preparationTime = "12 min"
        ),
        Food(
            id = FoodId(7),
            name = "Sushi Roll Especial",
            description = "Roll especial con salmón, palta y pepino cubierto con tobiko y salsa teriyaki, servido con jengibre encurtido y wasabi.",
            category = "Sushi",
            price = 35.00,
            rating = 4.7f,
            imageUrl = "food_sushi",
            preparationTime = "20 min"
        ),
        Food(
            id = FoodId(8),
            name = "Lomo Saltado",
            description = "Clásico plato peruano con tiras de lomo de res salteadas con tomate, cebolla y ají amarillo, acompañado de arroz y papas fritas.",
            category = "Peruana",
            price = 30.00,
            rating = 4.8f,
            imageUrl = "food_lomo",
            preparationTime = "20 min",
            isAvailable = true
        )
    )
}
