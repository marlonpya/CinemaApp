package com.microsol.myappzegel.presentation.fooddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsol.myappzegel.domain.repository.FoodRepository

// Factory needed to pass constructor params to the ViewModel
class FoodDetailViewModelFactory(
    private val foodRepository: FoodRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodDetailViewModel::class.java)) {
            return FoodDetailViewModel(foodRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
