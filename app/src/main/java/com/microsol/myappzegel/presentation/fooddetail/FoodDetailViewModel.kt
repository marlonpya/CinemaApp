package com.microsol.myappzegel.presentation.fooddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.microsol.myappzegel.data.model.Food
import com.microsol.myappzegel.data.model.FoodId
import com.microsol.myappzegel.domain.usecase.GetFoodsUseCase

class FoodDetailViewModel(
    private val getFoodsUseCase: GetFoodsUseCase
) : ViewModel() {

    private val _food = MutableLiveData<Food?>()
    val food: LiveData<Food?> = _food

    private val _orderAdded = MutableLiveData<Boolean>(false)
    val orderAdded: LiveData<Boolean> = _orderAdded

    fun loadFood(id: Int) {
        _food.value = getFoodsUseCase(FoodId(id))
    }

    // Called when "Add to Order" is tapped
    fun addToOrder() {
        _orderAdded.value = true
    }
}
