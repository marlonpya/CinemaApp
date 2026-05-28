package com.microsol.myappzegel.domain.repository

import com.microsol.myappzegel.domain.model.Movie

// Repository contract — SOLID: Dependency Inversion
// Presentation depends on this interface, not on the concrete implementation
interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieById(id: Int): Movie?
}
