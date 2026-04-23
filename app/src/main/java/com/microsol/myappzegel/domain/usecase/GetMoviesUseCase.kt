package com.microsol.myappzegel.domain.usecase

import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId
import com.microsol.myappzegel.data.repository.MovieRepository

// ─────────────────────────────────────────────────────────────────────────────
// WHAT IS A USE CASE?
//
// A use case (also called "interactor") encapsulates a single business action.
// It sits in the domain layer — the most stable part of Clean Architecture —
// and knows nothing about Android, databases, or UI.
//
// Benefits for students:
//   • Testable in isolation (no Android dependencies)
//   • One class = one job (Single Responsibility Principle)
//   • Readable: the name tells you exactly what the app does
//
// KOTLIN CONCEPT: operator fun invoke()
//
// Overloading the `invoke` operator lets us call the use case as if it were
// a function: `getMoviesUseCase()` instead of `getMoviesUseCase.execute()`.
// This is idiomatic Kotlin and makes ViewModels very readable.
// ─────────────────────────────────────────────────────────────────────────────
class GetMoviesUseCase(
    // KOTLIN CONCEPT: Constructor with a val parameter
    // The repository is injected via the constructor (manual DI).
    // We depend on the interface, not the implementation — Dependency Inversion.
    private val repository: MovieRepository
) {
    // Returns all movies from the repository
    operator fun invoke(): List<Movie> = repository.getMovies()

    // Overload: find a single movie by id
    operator fun invoke(id: MovieId): Movie? = repository.getMovieById(id)
}
