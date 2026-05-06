package com.microsol.myappzegel.data.remote

import com.microsol.myappzegel.data.remote.dto.CreditsResponse
import com.microsol.myappzegel.data.remote.dto.GenreListResponse
import com.microsol.myappzegel.data.remote.dto.MovieDetailDto
import com.microsol.myappzegel.data.remote.dto.MovieListResponse
import com.microsol.myappzegel.data.remote.dto.TvDetailDto
import com.microsol.myappzegel.data.remote.dto.TvListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "es-PE",
        @Query("page")     page: Int = 1
    ): MovieListResponse

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("language") language: String = "es-PE",
        @Query("page")     page: Int = 1
    ): TvListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")  id: Int,
        @Query("language") language: String = "es-PE"
    ): MovieDetailDto

    @GET("tv/{tv_id}")
    suspend fun getTvDetails(
        @Path("tv_id")     id: Int,
        @Query("language") language: String = "es-PE"
    ): TvDetailDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") id: Int
    ): CreditsResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "es-PE"
    ): GenreListResponse

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query")    query: String,
        @Query("language") language: String = "es-PE"
    ): MovieListResponse
}
