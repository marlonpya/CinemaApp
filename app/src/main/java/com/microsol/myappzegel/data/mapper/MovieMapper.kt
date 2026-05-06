package com.microsol.myappzegel.data.mapper

import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId
import com.microsol.myappzegel.data.remote.dto.MovieDetailDto
import com.microsol.myappzegel.data.remote.dto.MovieDto
import com.microsol.myappzegel.data.remote.dto.TvDetailDto
import com.microsol.myappzegel.data.remote.dto.TvShowDto

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

// ─── Movie list item → domain Movie ──────────────────────────────────────────

fun MovieDto.toDomain(genreMap: Map<Int, String> = emptyMap()): Movie {
    val genreName = genreIds.firstOrNull()?.let { genreMap[it] } ?: ""
    return Movie(
        id          = MovieId(id),
        title       = title,
        description = overview,
        genre       = genreName,
        year        = releaseDate.take(4).toIntOrNull() ?: 0,
        rating      = (voteAverage / 2).toFloat(),
        imageUrl    = if (!posterPath.isNullOrBlank()) "$POSTER_BASE_URL$posterPath" else "",
        director    = "",
        duration    = ""
    )
}

// ─── Movie detail → domain Movie ─────────────────────────────────────────────

fun MovieDetailDto.toDomain(director: String = ""): Movie {
    val genreName = genres.firstOrNull()?.name ?: ""
    val durationStr = runtime?.let { mins ->
        val h = mins / 60
        val m = mins % 60
        if (h > 0) "${h}h ${m}min" else "${m}min"
    } ?: ""
    return Movie(
        id          = MovieId(id),
        title       = title,
        description = overview,
        genre       = genreName,
        year        = releaseDate.take(4).toIntOrNull() ?: 0,
        rating      = (voteAverage / 2).toFloat(),
        imageUrl    = if (!posterPath.isNullOrBlank()) "$POSTER_BASE_URL$posterPath" else "",
        director    = director,
        duration    = durationStr
    )
}

// ─── TV show list item → domain Movie ────────────────────────────────────────

fun TvShowDto.toDomain(genreMap: Map<Int, String> = emptyMap()): Movie {
    val genreName = genreIds.firstOrNull()?.let { genreMap[it] } ?: ""
    return Movie(
        id          = MovieId(id),
        title       = name,
        description = overview,
        genre       = genreName,
        year        = firstAirDate.take(4).toIntOrNull() ?: 0,
        rating      = (voteAverage / 2).toFloat(),
        imageUrl    = if (!posterPath.isNullOrBlank()) "$POSTER_BASE_URL$posterPath" else "",
        director    = "",
        duration    = ""
    )
}

// ─── TV show detail → domain Movie ───────────────────────────────────────────

fun TvDetailDto.toDomain(director: String = ""): Movie {
    val genreName = genres.firstOrNull()?.name ?: ""
    val episodeMins = episodeRunTime.firstOrNull() ?: 0
    val durationStr = if (episodeMins > 0) "${episodeMins}min por episodio" else ""
    return Movie(
        id          = MovieId(id),
        title       = name,
        description = overview,
        genre       = genreName,
        year        = firstAirDate.take(4).toIntOrNull() ?: 0,
        rating      = (voteAverage / 2).toFloat(),
        imageUrl    = if (!posterPath.isNullOrBlank()) "$POSTER_BASE_URL$posterPath" else "",
        director    = director,
        duration    = durationStr
    )
}
