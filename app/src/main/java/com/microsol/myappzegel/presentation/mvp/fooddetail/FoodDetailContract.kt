package com.microsol.myappzegel.presentation.mvp.fooddetail

import com.microsol.myappzegel.domain.model.Food

interface FoodDetailContract {

    interface View {
        fun showFood(food: Food)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showOrderConfirmation()
        fun close()
    }

    interface Presenter {
        fun loadFood(id: Int)
        fun onAddToOrderClicked()
        fun onBackClicked()
        fun onDestroy()
    }
}
