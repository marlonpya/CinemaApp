package com.microsol.myappzegel.domain.model

// Domain model — clean representation used by the UI
// data class auto-generates equals(), hashCode(), toString() and copy()
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val genre: String,
    val year: Int,
    val rating: Float,
    val imageUrl: String,
    val director: String,
    val duration: String
)
