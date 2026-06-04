package com.microsol.myappzegel.presentation.mvvm.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsol.myappzegel.domain.repository.FoodRepository
import com.microsol.myappzegel.domain.repository.MovieRepository

// Factory needed to pass constructor params to the ViewModel
// @Suppress silences the unchecked cast — safe because we verify the class type first
class HomeViewModelFactory(
    private val movieRepository: MovieRepository,
    private val foodRepository: FoodRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(movieRepository, foodRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
