package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.remote.TmdbApiService
import com.microsol.myappzegel.domain.model.Movie
import com.microsol.myappzegel.domain.repository.MovieRepository

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

// Concrete implementation — fetches data from the network and maps to domain model
class MovieRepositoryImpl(
    private val api: TmdbApiService
) : MovieRepository {

    override suspend fun getMovies(): List<Movie> {
        return api.getPopularMovies().results.map { dto ->
            Movie(
                id          = dto.id,
                title       = dto.title,
                description = dto.overview,
                genre       = "",
                year        = dto.releaseDate.take(4).toIntOrNull() ?: 0,
                rating      = (dto.voteAverage / 2).toFloat(),
                imageUrl    = if (!dto.posterPath.isNullOrBlank()) "$POSTER_BASE_URL${dto.posterPath}" else "",
                director    = "",
                duration    = ""
            )
        }
    }

    override suspend fun getMovieById(id: Int): Movie? {
        return try {
            val detail = api.getMovieDetails(id)
            val genre = detail.genres.firstOrNull()?.name ?: ""
            val duration = detail.runtime?.let { mins ->
                val h = mins / 60
                val m = mins % 60
                if (h > 0) "${h}h ${m}min" else "${m}min"
            } ?: ""
            Movie(
                id          = detail.id,
                title       = detail.title,
                description = detail.overview,
                genre       = genre,
                year        = detail.releaseDate.take(4).toIntOrNull() ?: 0,
                rating      = (detail.voteAverage / 2).toFloat(),
                imageUrl    = if (!detail.posterPath.isNullOrBlank()) "$POSTER_BASE_URL${detail.posterPath}" else "",
                director    = "",
                duration    = duration
            )
        } catch (e: Exception) {
            null
        }
    }
}
