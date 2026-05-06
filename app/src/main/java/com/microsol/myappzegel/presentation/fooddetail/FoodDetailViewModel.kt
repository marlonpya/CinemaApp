package com.microsol.myappzegel.presentation.fooddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase
import kotlinx.coroutines.launch

class FoodDetailViewModel(
    private val getFoodsUseCase: GetFoodsUseCase
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
                _food.value = getFoodsUseCase(FoodId(id))
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
