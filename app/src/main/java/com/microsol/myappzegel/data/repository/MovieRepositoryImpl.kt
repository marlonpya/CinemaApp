package com.microsol.myappzegel.data.repository

import com.microsol.myappzegel.data.mapper.toDomain
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId
import com.microsol.myappzegel.data.remote.TmdbApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MovieRepositoryImpl(
    private val api: TmdbApiService
) : MovieRepository {

    // Genre map cached after the first call — avoids repeated network requests
    private var genreMap: Map<Int, String> = emptyMap()

    override suspend fun getMovies(): List<Movie> = coroutineScope {
        // Load genres once; harmless to re-fetch if the map is already populated
        if (genreMap.isEmpty()) {
            runCatching { api.getMovieGenres() }
                .onSuccess { genreMap = it.genres.associate { g -> g.id to g.name } }
        }

        // Fetch popular movies and popular TV shows in parallel
        val moviesDeferred  = async { runCatching { api.getPopularMovies().results } }
        val tvShowsDeferred = async { runCatching { api.getPopularTvShows().results } }

        val movies  = moviesDeferred.await().getOrElse { emptyList() }
            .map { it.toDomain(genreMap) }

        val tvShows = tvShowsDeferred.await().getOrElse { emptyList() }
            .map { it.toDomain(genreMap) }

        // Merge and sort by rating descending so the best content surfaces first
        (movies + tvShows).sortedByDescending { it.rating }
    }

    override suspend fun getMovieById(id: MovieId): Movie? = coroutineScope {
        // Try movie endpoint first; fall back to TV endpoint if it fails
        val movieResult = runCatching {
            val detailDeferred  = async { api.getMovieDetails(id.value) }
            val creditsDeferred = async { api.getMovieCredits(id.value) }

            val detail  = detailDeferred.await()
            val credits = creditsDeferred.await()
            val director = credits.crew
                .firstOrNull { it.job == "Director" }
                ?.name ?: ""

            detail.toDomain(director)
        }

        if (movieResult.isSuccess) {
            return@coroutineScope movieResult.getOrNull()
        }

        // Fall back to TV show details (series don't have a /credits endpoint in the same way)
        runCatching {
            api.getTvDetails(id.value).toDomain()
        }.getOrNull()
    }
}
