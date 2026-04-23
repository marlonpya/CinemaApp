package com.microsol.myappzegel.presentation.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase

class MovieDetailViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> = _movie

    // Called by the Activity passing the Int id received from the Intent extra
    fun loadMovie(id: Int) {
        // Wraps the raw Int in the type-safe MovieId value class
        _movie.value = getMoviesUseCase(MovieId(id))
    }
}
