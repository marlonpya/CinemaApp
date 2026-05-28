package com.microsol.myappzegel.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsol.myappzegel.domain.model.Food
import com.microsol.myappzegel.domain.model.Movie
import com.microsol.myappzegel.domain.repository.FoodRepository
import com.microsol.myappzegel.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository,
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> = _foods

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                coroutineScope {
                    val moviesDeferred = async {
                        movieRepository.getMovies()
                    }

                    val foodsDeferred = async {
                        foodRepository.getFoods()
                    }

                    _movies.value = moviesDeferred.await()
                    _foods.value = foodsDeferred.await()
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar los datos"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
