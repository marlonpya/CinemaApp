package com.microsol.myappzegel.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsol.myappzegel.domain.repository.MovieRepository

// Factory needed to pass constructor params to the ViewModel
class MovieDetailViewModelFactory(
    private val movieRepository: MovieRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
