package com.microsol.myappzegel.data.model

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: @JvmInline value class
//
// A value class wraps a single primitive without extra heap allocation at
// runtime (the compiler "inlines" the wrapped value). Using MovieId instead
// of a raw Int makes our code self-documenting and type-safe: you can't
// accidentally pass a FoodId where a MovieId is expected.
// ─────────────────────────────────────────────────────────────────────────────
@JvmInline
value class MovieId(val value: Int)

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: data class
//
// A data class automatically generates equals(), hashCode(), toString(), and
// copy() based on all primary-constructor properties. Perfect for models that
// only hold data with no business logic.
//
// KOTLIN CONCEPT: val vs var
//   val  → immutable (read-only) reference — use for fields that never change
//   var  → mutable reference — avoid unless mutation is intentional
//
// All Movie fields are val because a movie's data doesn't change once loaded.
// ─────────────────────────────────────────────────────────────────────────────
data class Movie(
    val id: MovieId,           // wrapped Int → type-safe identifier
    val title: String,
    val description: String,
    val genre: String,
    val year: Int,
    val rating: Float,         // e.g. 4.5 out of 5.0
    val imageUrl: String,      // drawable resource name used as placeholder
    val director: String,
    val duration: String       // e.g. "2h 32min"
)
