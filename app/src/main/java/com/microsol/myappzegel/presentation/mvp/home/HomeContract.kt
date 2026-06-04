package com.microsol.myappzegel.presentation.mvp.home

import com.microsol.myappzegel.domain.model.Food
import com.microsol.myappzegel.domain.model.Movie

// Contract centralizes the interface definitions for both View and Presenter
// — makes the MVP contract explicit and visible in one place
interface HomeContract {

    interface View {
        fun showMovies(movies: List<Movie>)
        fun showFoods(foods: List<Food>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun navigateToMovieDetail(id: Int)
        fun navigateToFoodDetail(id: Int)
    }

    interface Presenter {
        fun loadData()
        fun onMovieClicked(id: Int)
        fun onFoodClicked(id: Int)
        fun onDestroy()
    }
}
