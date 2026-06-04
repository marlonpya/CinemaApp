package com.microsol.myappzegel.presentation.mvp.moviedetail

import com.microsol.myappzegel.domain.model.Movie

interface MovieDetailContract {

    interface View {
        fun showMovie(movie: Movie)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun close()
    }

    interface Presenter {
        fun loadMovie(id: Int)
        fun onBackClicked()
        fun onDestroy()
    }
}
