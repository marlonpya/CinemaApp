package com.microsol.myappzegel.data.remote.dto

import com.google.gson.annotations.SerializedName

// ─── TheMealDB meal item ──────────────────────────────────────────────────────
// All field names in TheMealDB use strXxx / idXxx prefixes.

data class MealDto(
    @SerializedName("idMeal")            val idMeal: String,
    @SerializedName("strMeal")           val strMeal: String,
    @SerializedName("strCategory")       val strCategory: String?,
    @SerializedName("strArea")           val strArea: String?,
    @SerializedName("strInstructions")   val strInstructions: String?,
    @SerializedName("strMealThumb")      val strMealThumb: String?,

    // Ingredients (up to 20 slots; null when not used)
    @SerializedName("strIngredient1")  val strIngredient1: String?,
    @SerializedName("strIngredient2")  val strIngredient2: String?,
    @SerializedName("strIngredient3")  val strIngredient3: String?,
    @SerializedName("strIngredient4")  val strIngredient4: String?,
    @SerializedName("strIngredient5")  val strIngredient5: String?,

    @SerializedName("strMeasure1")  val strMeasure1: String?,
    @SerializedName("strMeasure2")  val strMeasure2: String?,
    @SerializedName("strMeasure3")  val strMeasure3: String?,
    @SerializedName("strMeasure4")  val strMeasure4: String?,
    @SerializedName("strMeasure5")  val strMeasure5: String?
) {
    fun ingredientsSummary(): String {
        val pairs = listOf(
            strIngredient1 to strMeasure1,
            strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5
        )
        return pairs
            .filter { (ing, _) -> !ing.isNullOrBlank() }
            .joinToString(", ") { (ing, meas) ->
                if (!meas.isNullOrBlank()) "${meas.trim()} $ing" else ing!!
            }
    }
}

// ─── Response wrapper (both /random.php and /lookup.php use the same shape) ──

data class MealListResponse(
    @SerializedName("meals") val meals: List<MealDto>?
)
