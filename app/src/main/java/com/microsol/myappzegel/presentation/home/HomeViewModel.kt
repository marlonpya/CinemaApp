package com.microsol.myappzegel.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.Movie
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase
import com.microsol.myappzegel.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getFoodsUseCase: GetFoodsUseCase
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
                _movies.value = getMoviesUseCase()
                _foods.value  = getFoodsUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar los datos"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
