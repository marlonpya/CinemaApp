package com.microsol.myappzegel.presentation.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.data.model.MovieId
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> = _movie

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _movie.value = getMoviesUseCase(MovieId(id))
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar la película"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
