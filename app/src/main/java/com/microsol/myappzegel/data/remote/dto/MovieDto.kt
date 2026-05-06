package com.microsol.myappzegel.data.remote.dto

import com.google.gson.annotations.SerializedName

// ─── Movie list item (from /movie/popular) ───────────────────────────────────

data class MovieDto(
    @SerializedName("id")           val id: Int,
    @SerializedName("title")        val title: String,
    @SerializedName("overview")     val overview: String,
    @SerializedName("poster_path")  val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genre_ids")    val genreIds: List<Int>
)

// ─── Movie detail (from /movie/{id}) — superset of MovieDto ──────────────────

data class MovieDetailDto(
    @SerializedName("id")           val id: Int,
    @SerializedName("title")        val title: String,
    @SerializedName("overview")     val overview: String,
    @SerializedName("poster_path")  val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime")      val runtime: Int?,
    @SerializedName("genres")       val genres: List<GenreDto>
)

// ─── TV show list item (from /tv/popular) ────────────────────────────────────

data class TvShowDto(
    @SerializedName("id")             val id: Int,
    @SerializedName("name")           val name: String,
    @SerializedName("overview")       val overview: String,
    @SerializedName("poster_path")    val posterPath: String?,
    @SerializedName("vote_average")   val voteAverage: Double,
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("genre_ids")      val genreIds: List<Int>
)

// ─── TV show detail (from /tv/{id}) ──────────────────────────────────────────

data class TvDetailDto(
    @SerializedName("id")              val id: Int,
    @SerializedName("name")            val name: String,
    @SerializedName("overview")        val overview: String,
    @SerializedName("poster_path")     val posterPath: String?,
    @SerializedName("vote_average")    val voteAverage: Double,
    @SerializedName("first_air_date")  val firstAirDate: String,
    @SerializedName("episode_run_time") val episodeRunTime: List<Int>,
    @SerializedName("genres")          val genres: List<GenreDto>
)

// ─── Genres ──────────────────────────────────────────────────────────────────

data class GenreDto(
    @SerializedName("id")   val id: Int,
    @SerializedName("name") val name: String
)

data class GenreListResponse(
    @SerializedName("genres") val genres: List<GenreDto>
)

// ─── Response wrappers ───────────────────────────────────────────────────────

data class MovieListResponse(
    @SerializedName("results") val results: List<MovieDto>
)

data class TvListResponse(
    @SerializedName("results") val results: List<TvShowDto>
)

// ─── Credits (from /movie/{id}/credits) ──────────────────────────────────────

data class CreditsResponse(
    @SerializedName("crew") val crew: List<CrewMemberDto>
)

data class CrewMemberDto(
    @SerializedName("name") val name: String,
    @SerializedName("job")  val job: String
)
