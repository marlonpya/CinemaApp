package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId

// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN CONCEPT: Interface
// SOLID PRINCIPLE: Dependency Inversion (D)
//
// We define a contract (interface) here in the data layer. The domain and
// presentation layers depend on this abstraction, NOT on the concrete
// implementation. This means we can swap MovieRepositoryImpl for a network
// version later without touching use-cases or ViewModels.
// ─────────────────────────────────────────────────────────────────────────────
interface MovieRepository {
    fun getMovies(): List<Movie>
    fun getMovieById(id: MovieId): Movie?
}
