package com.microsol.myappzegel.presentation.fooddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsol.myappzegel.domain.model.Food
import com.microsol.myappzegel.domain.repository.FoodRepository
import kotlinx.coroutines.launch

class FoodDetailViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _food = MutableLiveData<Food?>()
    val food: LiveData<Food?> = _food

    private val _orderAdded = MutableLiveData<Boolean>(false)
    val orderAdded: LiveData<Boolean> = _orderAdded

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadFood(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _food.value = foodRepository.getFoodById(id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar el plato"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToOrder() {
        _orderAdded.value = true
    }
}
