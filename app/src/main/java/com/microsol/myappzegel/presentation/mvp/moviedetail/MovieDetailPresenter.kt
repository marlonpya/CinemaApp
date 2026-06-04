package com.microsol.myappzegel.presentation.mvp.moviedetail

import com.microsol.myappzegel.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MovieDetailPresenter(
    private val movieRepository: MovieRepository
) : MovieDetailContract.Presenter {

    private var view: MovieDetailContract.View? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun attachView(view: MovieDetailContract.View) {
        this.view = view
    }

    override fun loadMovie(id: Int) {
        view?.showLoading()
        scope.launch {
            try {
                val movie = movieRepository.getMovieById(id)
                view?.hideLoading()
                if (movie != null) {
                    view?.showMovie(movie)
                } else {
                    view?.showError("Película no encontrada")
                }
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError(e.message ?: "Error al cargar la película")
            }
        }
    }

    override fun onBackClicked() {
        view?.close()
    }

    override fun onDestroy() {
        view = null
        scope.cancel()
    }
}
