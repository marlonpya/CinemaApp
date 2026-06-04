package com.microsol.myappzegel.presentation.mvp.home

import com.microsol.myappzegel.domain.repository.FoodRepository
import com.microsol.myappzegel.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// Presenter owns the logic — the View only renders and forwards user events
// SupervisorJob keeps sibling coroutines alive if one fails
class HomePresenter(
    private val movieRepository: MovieRepository,
    private val foodRepository: FoodRepository
) : HomeContract.Presenter {

    private var view: HomeContract.View? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun attachView(view: HomeContract.View) {
        this.view = view
    }

    override fun loadData() {
        view?.showLoading()
        scope.launch {
            try {
                coroutineScope {
                    val moviesDeferred = async { movieRepository.getMovies() }
                    val foodsDeferred = async { foodRepository.getFoods() }
                    val movies = moviesDeferred.await()
                    val foods = foodsDeferred.await()
                    view?.hideLoading()
                    view?.showMovies(movies)
                    view?.showFoods(foods)
                }
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError(e.message ?: "Error al cargar los datos")
            }
        }
    }

    override fun onMovieClicked(id: Int) {
        view?.navigateToMovieDetail(id)
    }

    override fun onFoodClicked(id: Int) {
        view?.navigateToFoodDetail(id)
    }

    // Called from Activity.onDestroy() — prevents memory leaks and cancels in-flight coroutines
    override fun onDestroy() {
        view = null
        scope.cancel()
    }
}
