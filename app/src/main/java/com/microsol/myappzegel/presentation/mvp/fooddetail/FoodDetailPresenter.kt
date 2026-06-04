package com.microsol.myappzegel.presentation.mvp.fooddetail

import com.microsol.myappzegel.domain.repository.FoodRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FoodDetailPresenter(
    private val foodRepository: FoodRepository
) : FoodDetailContract.Presenter {

    private var view: FoodDetailContract.View? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun attachView(view: FoodDetailContract.View) {
        this.view = view
    }

    override fun loadFood(id: Int) {
        view?.showLoading()
        scope.launch {
            try {
                val food = foodRepository.getFoodById(id)
                view?.hideLoading()
                if (food != null) {
                    view?.showFood(food)
                } else {
                    view?.showError("Plato no encontrado")
                }
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError(e.message ?: "Error al cargar el plato")
            }
        }
    }

    override fun onAddToOrderClicked() {
        view?.showOrderConfirmation()
    }

    override fun onBackClicked() {
        view?.close()
    }

    override fun onDestroy() {
        view = null
        scope.cancel()
    }
}
